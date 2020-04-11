package org.mimosaframework.orm.platform.sqlite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.Keyword;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.utils.DatabaseTypes;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SqliteDatabasePorter extends AbstractDatabasePorter {
    private static final Log logger = LogFactory.getLog(SqliteDatabasePorter.class);
    private static DifferentColumn differentColumn = new SqliteDifferentColumn();

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
    protected DatabaseTypes getDatabaseType() {
        return DatabaseTypes.SQLITE;
    }

    @Override
    public void createTable(MappingTable table) throws SQLException {
        SQLBuilder fieldBuilder = this.createTableFields(table);


        // 开始创建主键列
        Set<MappingField> fields = table.getMappingFields();
        List<MappingField> primaryKeys = table.getMappingPrimaryKeyFields();
        if (primaryKeys != null && primaryKeys.size() > 1) {
            if (fields.size() > 0) {
                fieldBuilder.addSplit();
            }
            fieldBuilder.PRIMARY().KEY();
            String[] names = new String[primaryKeys.size()];
            for (int i = 0; i < primaryKeys.size(); i++) {
                names[i] = primaryKeys.get(i).getMappingColumnName();
            }
            fieldBuilder.addParenthesisWrapString(names);
        }


        SQLBuilder tableBuilder = this.createSQLBuilder();
        tableBuilder.CREATE().TABLE(null).IF().NOT().EXISTS()
                .addString(table.getMappingTableName());

        tableBuilder.symbolParenthesis(fieldBuilder);
        String encoding = table.getEncoding();
        this.createTableDefaultCharset(tableBuilder, encoding);

        PorterStructure tableStructure = new PorterStructure(TypeForRunner.CREATE_TABLE, tableBuilder);

        carryHandler.doHandler(tableStructure);
    }


    protected SQLBuilder createTableFields(MappingTable table) {
        SQLBuilder fieldBuilder = this.createSQLBuilder();
        // 开始创建表字段
        Set<MappingField> fields = table.getMappingFields();
        Iterator<MappingField> iterator = fields.iterator();

        List<MappingField> primaryKeys = table.getMappingPrimaryKeyFields();

        while (iterator.hasNext()) {
            MappingField field = iterator.next();
            fieldBuilder.addWrapString(field.getMappingColumnName());
            if (primaryKeys == null || primaryKeys.size() == 1) {
                this.buildTableField(field, fieldBuilder, true, true);
            } else {
                this.buildTableField(field, fieldBuilder, false, true);
            }
            if (iterator.hasNext()) {
                fieldBuilder.addSplit();
            }
        }
        return fieldBuilder;
    }

    protected void setTimeForUpdateField(MappingField field, SQLBuilder builder) {
        builder.addString("timestamp");
        if (StringTools.isNotEmpty(field.getMappingFieldComment())) {
            builder.COMMENT().addQuotesString(field.getMappingFieldComment());
        }
    }

    /**
     * column_1 int constraint table_name_pk primary key autoincrement
     *
     * @param builder
     * @param field
     * @param isAutoIncrement
     */
    @Override
    protected void buildTableFieldAuthIncrement(SQLBuilder builder, MappingField field, boolean isAutoIncrement) {
        if (field.isMappingFieldAutoIncrement() && isAutoIncrement) {
            builder.addString("autoincrement");
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
                    throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                            SqliteDatabasePorter.class, "not_fount_field"));
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

        List<Long> ids = (List<Long>) carryHandler.doHandler(new PorterStructure(TypeForRunner.ADD_OBJECTS, insertBuilder));
        return ids;
    }
}
