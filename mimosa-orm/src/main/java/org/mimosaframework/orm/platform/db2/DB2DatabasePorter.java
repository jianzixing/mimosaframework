package org.mimosaframework.orm.platform.db2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.Keyword;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DB2DatabasePorter extends AbstractDatabasePorter {
    private static final Log logger = LogFactory.getLog(DB2DatabasePorter.class);
    private static DifferentColumn differentColumn = new DB2DifferentColumn();

    @Override
    protected DifferentColumn getDifferentColumn() {
        return differentColumn;
    }

    @Override
    protected SQLBuilder createSQLBuilder() {
        SQLBuilder builder = SQLBuilderFactory.createQMSQLBuilder();
        return builder;
    }

    @Override
    public void createTable(MappingTable table) throws SQLException {
        SQLBuilder fieldBuilder = this.createTableFields(table);

        SQLBuilder alertMultiPk = this.createSQLBuilder();
        String pkName = table.getMappingTableName() + "_pk";
        alertMultiPk.ALTER().TABLE(table.getMappingTableName()).ADD()
                .addString("constraint").addString(pkName);


        // 开始创建主键列
        Set<MappingField> fields = table.getMappingFields();
        List<MappingField> primaryKeys = table.getMappingPrimaryKeyFields();
        if (primaryKeys != null && primaryKeys.size() > 0) {
            if (fields.size() > 0) {
                alertMultiPk.addSplit();
            }
            alertMultiPk.PRIMARY().KEY();
            String[] names = new String[primaryKeys.size()];
            for (int i = 0; i < primaryKeys.size(); i++) {
                names[i] = primaryKeys.get(i).getMappingColumnName();
            }
            alertMultiPk.addParenthesisWrapString(names);
        }


        SQLBuilder tableBuilder = this.createSQLBuilder();
        tableBuilder.CREATE().TABLE(null).addString(table.getMappingTableName());

        tableBuilder.symbolParenthesis(fieldBuilder);
        String encoding = table.getEncoding();
        this.createTableDefaultCharset(tableBuilder, encoding);

        PorterStructure tableStructure = new PorterStructure(ChangerClassify.CREATE_TABLE, tableBuilder);
        PorterStructure fieldStructure = new PorterStructure(ChangerClassify.UPDATE_FIELD, alertMultiPk);

        carryHandler.doHandler(tableStructure);
        carryHandler.doHandler(fieldStructure);
    }

    protected void buildHasLengthTableField(boolean hasLength, MappingField field, SQLBuilder builder) {
        if (hasLength && !field.isMappingAutoIncrement()) {
            if (field.getMappingFieldDecimalDigits() == 0) {
                builder.addParenthesisString("" + field.getMappingFieldLength());
            } else {
                int maxLength = field.getMappingFieldLength();
                if (maxLength > 31) {
                    maxLength = 31;
                    logger.warn("DB2的Decimal类型整数位最大只支持31位");
                }
                builder.addParenthesisString("" + maxLength + "," + field.getMappingFieldDecimalDigits());
            }
        }
    }

    protected void setTimeForUpdateField(MappingField field, SQLBuilder builder) {
        builder.addString("timestamp");
        if (StringTools.isNotEmpty(field.getMappingFieldComment())) {
            builder.COMMENT().addQuotesString(field.getMappingFieldComment());
        }
    }

    /**
     * id int generated always as identity
     * id int constraint table_name_pk primary key
     *
     * @param builder
     * @param field
     * @param isAutoIncrement
     */
    @Override
    protected void buildTableFieldAuthIncrement(SQLBuilder builder, MappingField field, boolean isAutoIncrement) {
        if (field.isMappingFieldAutoIncrement() && isAutoIncrement) {
            builder.addString("generated")
                    .addString("always")
                    .addString("as")
                    .addString("identity");
        }
    }

    @Override
    protected void buildTableFieldPrimaryKey(SQLBuilder builder, MappingField field, boolean isAddPrimaryKey) {
        if (field.isMappingFieldPrimaryKey() && isAddPrimaryKey) {
            String pkName = field.getMappingColumnName() + "_pk";
            if (field.getMappingTable() != null) {
                pkName = field.getMappingTable().getMappingTableName() + "_pk";
            }
            builder.addString("constraint")
                    .addString(pkName)
                    .PRIMARY().KEY();
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
}
