package org.mimosaframework.orm.platform.postgresql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.LimitBuilder;

import java.sql.SQLException;
import java.util.*;

public class PostgreSQLDatabasePorter extends AbstractDatabasePorter {
    private static final Log logger = LogFactory.getLog(PostgreSQLDatabasePorter.class);
    private static DifferentColumn differentColumn = new PostgreSQLDifferentColumn();

    protected void buildTableField(MappingField field,
                                   SQLBuilder builder,
                                   boolean isAddPrimaryKey,
                                   boolean isAutoIncrement) {
        boolean isTimeForUpdate = field.isMappingFieldTimeForUpdate();
        if (isTimeForUpdate) {
            this.setTimeForUpdateField(field, builder);
        } else {
            String typeName = differentColumn.getTypeNameByClass(field.getMappingFieldType());
            if (field.isMappingFieldAutoIncrement() && isAutoIncrement) {
                builder.addString("SERIAL");
            } else {
                // 如果字段是自增列，在不同数据库中支持的类型不同
                if (field.isMappingAutoIncrement() && isAutoIncrement) {
                    typeName = differentColumn.getAutoIncrementTypeNameByClass(field.getMappingFieldType());
                }
                builder.addString(typeName);

                boolean hasLength = differentColumn.typeHasLength(field.getMappingFieldType());
                this.buildHasLengthTableField(hasLength, field, builder);
            }

            if (field.isMappingFieldPrimaryKey() && isAddPrimaryKey) {
                builder.PRIMARY().KEY();
            }

            if (!field.isMappingFieldNullable()) {
                builder.NOT().NULL();
            }

            if (field.isMappingFieldUnique()) {
                builder.UNIQUE();
            }

            if (StringTools.isNotEmpty(field.getMappingFieldDefaultValue())) {
                builder.DEFAULT().addQuotesString(field.getMappingFieldDefaultValue());
            }
            if (StringTools.isNotEmpty(field.getMappingFieldComment())) {
                builder.COMMENT().addQuotesString(field.getMappingFieldComment());
            }
        }
    }

    protected void setTimeForUpdateField(MappingField field, SQLBuilder builder) {
        //`modified_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
        builder.addString("timestamp");
        if (StringTools.isNotEmpty(field.getMappingFieldComment())) {
            builder.COMMENT().addQuotesString(field.getMappingFieldComment());
        }
    }

    @Override
    protected DifferentColumn getDifferentColumn() {
        return differentColumn;
    }

    protected SQLBuilder buildLimit(DefaultQuery query) {
        Limit o = query.getLimit();
        if (o != null) {
            SQLBuilder limitBuilder = SQLBuilderFactory.createQMSQLBuilder();

            long start = o.getStart();
            long limit = this.getLimitDefault(query, o);

            if (start > 0 || limit > 0) {
                limitBuilder.LIMIT()
                        .addDataPlaceholder("Page Start", limit)
                        .addString("OFFSET")
                        .addDataPlaceholder("Page Limit", start);
            }
            return limitBuilder;
        }
        return null;
    }

    @Override
    public void createTable(MappingTable table) throws SQLException {
        SQLBuilder fieldBuilder = this.createTableFields(table);
        this.createTablePrimaryKeys(fieldBuilder, table);

        SQLBuilder tableBuilder = SQLBuilderFactory.createQMSQLBuilder();
        tableBuilder.CREATE().TABLE(null).IF().NOT().EXISTS()
                .addString(table.getMappingTableName());

        tableBuilder.symbolParenthesis(fieldBuilder);
        String encoding = table.getEncoding();
        this.createTableDefaultCharset(tableBuilder, encoding);

        PorterStructure tableStructure = new PorterStructure(ChangerClassify.CREATE_TABLE, tableBuilder);
        carryHandler.doHandler(tableStructure);
    }

    @Override
    public Long insert(MappingTable table, ModelObject object) throws SQLException {
        String tableName = table.getDatabaseTableName();
        SQLBuilder insertBuilder = this.createSQLBuilder().INSERT().INTO().addString(tableName);
        this.insertAddValue(insertBuilder, table, object);
        Long id = (Long) carryHandler.doHandler(new PorterStructure(ChangerClassify.ADD_OBJECT, insertBuilder));

        this.resetSeqValue(table, tableName, object);
        return id;
    }

    private void resetSeqValue(MappingTable table, String tableName, ModelObject object) {
        // 如果插入新数据带有主键值，这个时候就无法确认当前的自增序列和数据库
        // 中的主键知否重复，所以需要重置序列值。
        try {
            Set<MappingField> fields = table.getMappingFields();
            if (fields != null) {
                for (MappingField field : fields) {
                    if (field.isMappingAutoIncrement()) {
                        Object v = object.get(field.getMappingColumnName());
                        if (v != null && !"".equals(v)) {
                            // select max(id) from table
                            SQLBuilder max = this.createSQLBuilder().SELECT().addFun("max", field.getMappingColumnName(), "max")
                                    .FROM().addString(tableName);
                            List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(new PorterStructure(ChangerClassify.SELECT, max));
                            if (objects != null && objects.size() > 0) {
                                ModelObject o = objects.get(0);
                                Long maxValue = o.getLong("max");
                                if (maxValue != null) {
                                    maxValue = maxValue + 1;
                                    logger.warn(Messages.get(LanguageMessageFactory.PROJECT,
                                            PostgreSQLDatabasePorter.class, "reset_incr_field"));

                                    SQLBuilder resetSeq = this.createSQLBuilder().ALTER().addString("sequence")
                                            .addString(tableName + "_" + field.getMappingColumnName() + "_seq")
                                            .addString("restart").addString("with").addString("" + maxValue);
                                    carryHandler.doHandler(new PorterStructure(ChangerClassify.UPDATE, resetSeq));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    PostgreSQLDatabasePorter.class, "reset_incr_field_error"), e);
        }
    }

    @Override
    public List<Long> inserts(MappingTable table, List<ModelObject> objects) throws SQLException {
        String tableName = table.getDatabaseTableName();

        SQLBuilder insertBuilder = SQLBuilderFactory.createQMSQLBuilder().INSERT().INTO().addString(tableName);
        List<String> fields = this.clearAutoIncrement(table);
        insertBuilder.addParenthesisWrapString(fields.toArray(new String[]{}));
        insertBuilder.VALUES();

        Iterator<ModelObject> listIterator = objects.iterator();
        while (listIterator.hasNext()) {
            ModelObject object = listIterator.next();
            Iterator<String> iterator = fields.iterator();
            insertBuilder.addParenthesisStart();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = object.get(key);

                String fieldName = String.valueOf(key);
                MappingField mappingField = table.getMappingFieldByName(fieldName);
                if (mappingField == null) {
                    throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                            PostgreSQLDatabasePorter.class, "not_fount_field", fieldName));
                }

                this.addDataPlaceholder(insertBuilder, fieldName, value, mappingField);

                if (iterator.hasNext()) {
                    insertBuilder.addSplit();
                }
            }
            insertBuilder.addParenthesisEnd();
            if (listIterator.hasNext()) {
                insertBuilder.addSplit();
            }
        }

        if (objects != null) {
            for (ModelObject object : objects) {
                this.resetSeqValue(table, tableName, object);
            }
        }

        List<Long> ids = (List<Long>) carryHandler.doHandler(new PorterStructure(ChangerClassify.ADD_OBJECTS, insertBuilder));
        return ids;
    }

    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createQMSQLBuilder();
    }

    @Override
    protected void transformationSQLLimit(LimitBuilder limitBuilder, SQLBuilder sqlBuilder) {
        sqlBuilder.LIMIT().addString("" + limitBuilder.getLimit())
                .addString("OFFSET").addString("" + limitBuilder.getStart());
    }
}
