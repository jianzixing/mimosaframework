package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.*;

public class PostgreSQLDatabasePorter extends AbstractDatabasePorter {
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
                    throw new IllegalArgumentException("没有找到字段" + fieldName + "映射字段");
                }

                if (value == null || value == Keyword.NULL) {
                    insertBuilder.addString("NULL");
                } else {
                    insertBuilder.addDataPlaceholder(key, value);
                }

                if (iterator.hasNext()) {
                    insertBuilder.addSplit();
                }
            }
            insertBuilder.addParenthesisEnd();
            if (listIterator.hasNext()) {
                insertBuilder.addSplit();
            }
        }

        List<Long> ids = (List<Long>) carryHandler.doHandler(new PorterStructure(ChangerClassify.ADD_OBJECTS, insertBuilder));
        return ids;
    }

    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createQMSQLBuilder();
    }
}
