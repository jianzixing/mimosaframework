package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.alter.AlterFactory;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.create.DefaultSQLCreateBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class MysqlPlatformDialect extends PlatformDialect {
    public MysqlPlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INT");
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR");
        registerColumnType(KeyColumnType.CHAR, "CHAR");
        registerColumnType(KeyColumnType.TINYINT, "TINYINT");
        registerColumnType(KeyColumnType.SMALLINT, "SMALLINT");
        registerColumnType(KeyColumnType.BIGINT, "BIGINT");
        registerColumnType(KeyColumnType.FLOAT, "FLOAT");
        registerColumnType(KeyColumnType.DOUBLE, "DOUBLE");
        registerColumnType(KeyColumnType.DECIMAL, "DECIMAL");
        registerColumnType(KeyColumnType.BOOLEAN, "BOOLEAN");
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "TIME");
        registerColumnType(KeyColumnType.DATETIME, "DATETIME");
        registerColumnType(KeyColumnType.TIMESTAMP, "TIMESTAMP");

        registerColumnType(KeyColumnType.BLOB, "BLOB");
        registerColumnType(KeyColumnType.MEDIUMBLOB, "MEDIUMBLOB");
        registerColumnType(KeyColumnType.LONGBLOB, "LONGBLOB");
        registerColumnType(KeyColumnType.TEXT, "TEXT");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "MEDIUMTEXT");
        registerColumnType(KeyColumnType.LONGTEXT, "LONGTEXT");
    }

    @Override
    public SQLBuilderCombine alter(StampAlter alter) {
        StampCombineBuilder builder = new MysqlStampAlter();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine create(StampCreate create) {
        StampCombineBuilder builder = new MysqlStampCreate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, create);
        return combine;
    }

    @Override
    public SQLBuilderCombine drop(StampDrop drop) {
        StampCombineBuilder builder = new MysqlStampDrop();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, drop);
        return combine;
    }

    @Override
    public SQLBuilderCombine insert(StampInsert insert) {
        StampCombineBuilder builder = new MysqlStampInsert();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return combine;
    }

    @Override
    public SQLBuilderCombine delete(StampDelete delete) {
        StampCombineBuilder builder = new MysqlStampDelete();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, delete);
        return combine;
    }

    @Override
    public SQLBuilderCombine select(StampSelect select) {
        StampCombineBuilder builder = new MysqlStampSelect();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, select);
        return combine;
    }

    @Override
    public SQLBuilderCombine update(StampUpdate update) {
        StampCombineBuilder builder = new MysqlStampUpdate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, update);
        return combine;
    }

    @Override
    public void define(DataDefinition definition) throws SQLException {
        if (definition != null) {
            DataDefinitionType type = definition.getType();
            if (type == DataDefinitionType.CREATE_TABLE) {
                MappingTable mappingTable = definition.getMappingTable();
                StampCreate stampCreate = this.commonCreateTable(mappingTable);
                if (StringTools.isNotEmpty(mappingTable.getEngineName())) {
                    stampCreate.extra = "ENGINE=InnoDB";
                }
                this.runner(stampCreate);
                Set<MappingField> mappingFields = mappingTable.getMappingFields();
                for (MappingField mappingField : mappingFields) {
                    if (mappingField.isMappingFieldUnique() || mappingField.isMappingFieldIndex()) {
                        this.triggerIndex(definition.getMappingTable(),
                                null, mappingField, null);
                    }
                }
            }
            if (type == DataDefinitionType.DROP_TABLE) {
                TableStructure tableStructure = definition.getTableStructure();
                StampDrop stampDrop = this.commonDropTable(tableStructure);
                this.runner(stampDrop);
            }
            if (type == DataDefinitionType.ADD_COLUMN) {
                StampAlter stampAlter = this.commonAddColumn(definition.getMappingTable(), definition.getMappingField());
                this.runner(stampAlter);

                this.triggerIndex(definition.getMappingTable(), definition.getTableStructure(),
                        definition.getMappingField(), null);
            }
            if (type == DataDefinitionType.MODIFY_COLUMN) {
                TableStructure tableStructure = definition.getTableStructure();
                MappingTable mappingTable = definition.getMappingTable();
                MappingField mappingField = definition.getMappingField();
                TableColumnStructure columnStructure = definition.getColumnStructure();
                ColumnType columnType = this.getColumnType(JavaType2ColumnType
                        .getColumnTypeByJava(mappingField.getMappingFieldType()));

                String tableName = mappingTable.getMappingTableName();
                String columnName = mappingField.getMappingColumnName();
                DefaultSQLAlterBuilder sql = new DefaultSQLAlterBuilder();
                sql.alter().table(tableName).modify().column(columnName);

                List<ColumnEditType> columnEditTypes = this.compareColumnChange(tableStructure, mappingField, columnStructure);
                columnEditTypes.remove(ColumnEditType.AUTO_INCREMENT);
                columnEditTypes.remove(ColumnEditType.PRIMARY_KEY);

                boolean isPk = tableStructure.isPrimaryKeyColumn(columnStructure.getColumnName());
                if (columnEditTypes.size() > 0) {
                    // ColumnEditType.TYPE;
                    this.setSQLType(sql, mappingField.getMappingFieldType(),
                            mappingField.getMappingFieldLength(), mappingField.getMappingFieldDecimalDigits());

                    if (!isPk) {
                        if (!mappingField.isMappingFieldNullable()) {
                            sql.not();
                            sql.nullable();
                        }
                    }

                    String def = mappingField.getMappingFieldDefaultValue();
                    if (StringTools.isNotEmpty(def)) {
                        sql.defaultValue(def);
                    }

                    String cmt = mappingField.getMappingFieldComment();
                    if (StringTools.isNotEmpty(cmt)) {
                        sql.comment(cmt);
                    }

                    this.runner(sql.compile());
                    if (columnStructure != null) columnStructure.setState(1);
                }

                // 需要单独修改
                if (mappingField.isMappingAutoIncrement()) {
                    // ColumnEditType.AUTO_INCREMENT;
                }

                // 需要单独修改
                if (mappingField.isMappingFieldPrimaryKey() != isPk) {
                    // ColumnEditType.PRIMARY_KEY;
                }

                this.triggerIndex(definition.getMappingTable(), definition.getTableStructure(),
                        definition.getMappingField(), columnStructure);
            }
            if (type == DataDefinitionType.DROP_COLUMN) {
                TableColumnStructure columnStructure = definition.getColumnStructure();
                StampAlter stampAlter = this.commonDropColumn(definition.getMappingTable(), columnStructure);
                this.runner(stampAlter);
                if (columnStructure != null) {
                    columnStructure.setState(2);
                }
                this.triggerIndex(definition.getMappingTable(), definition.getTableStructure(),
                        definition.getMappingField(), definition.getColumnStructure());
            }
            if (type == DataDefinitionType.ADD_INDEX) {
                StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
                this.runner(sql);
            }
            if (type == DataDefinitionType.MODIFY_INDEX) {
                StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexName());
                this.runner(stampDrop);
                StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
                this.runner(sql);
            }
            if (type == DataDefinitionType.DROP_INDEX) {
                StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexName());
                this.runner(stampDrop);
            }
        }
    }

    @Override
    public void ending(MappingTable mappingTable, TableStructure tableStructure) throws SQLException {
        if (mappingTable != null && tableStructure != null) {
            // 校验表的自增列和主键
            this.triggerIncrAndKeys(mappingTable, tableStructure);
        }
    }

    @Override
    protected void rebuildPrimaryKey(MappingTable mappingTable, TableStructure tableStructure) throws SQLException {
        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        String[] fields = null;
        if (pks != null && pks.size() > 0) {
            fields = new String[pks.size()];
            for (int i = 0; i < pks.size(); i++) {
                fields[i] = pks.get(i).getMappingColumnName();
            }
        }

        List<TableConstraintStructure> oldPks = tableStructure.getPrimaryKey();
        if (oldPks != null && oldPks.size() > 0) {
            StampAction stampAction = AlterFactory.origin().alter().table(mappingTable.getMappingTableName())
                    .drop().primary().key().compile();
            this.runner(stampAction);
        }
        if (pks != null && pks.size() > 0) {
            StampAction stampAction = AlterFactory.origin().alter().table(mappingTable.getMappingTableName())
                    .add().primary().key().columns(fields).compile();
            this.runner(stampAction);
        }
    }

    @Override
    protected void rebuildAutoIncrement(MappingTable mappingTable, TableStructure tableStructure) {
        MappingField mappingField = mappingTable.getAutoIncrementField();
        if (mappingField != null && mappingField.isMappingFieldAutoIncrement()) {
            DefaultSQLAlterBuilder sql = AlterFactory.origin();
            sql.alter().table(mappingTable.getMappingTableName()).modify()
                    .column(mappingField.getMappingColumnName());
            this.setSQLType(sql, mappingField.getMappingFieldType(),
                    mappingField.getMappingFieldLength(), mappingField.getMappingFieldDecimalDigits());
            if (!mappingField.isMappingFieldNullable()) {
                sql.not().nullable();
            }

            if (mappingField.isMappingAutoIncrement()) {
                sql.autoIncrement();
            }

            String def = mappingField.getMappingFieldDefaultValue();
            if (StringTools.isNotEmpty(def)) {
                sql.defaultValue(def);
            }

            String cmt = mappingField.getMappingFieldComment();
            if (StringTools.isNotEmpty(cmt)) {
                sql.comment(cmt);
            }
        }
    }

    @Override
    protected void createIndex(MappingTable mappingTable, MappingField mappingField, boolean unique) {
        System.out.println();
    }

    @Override
    protected void dropIndex(MappingTable mappingTable, MappingField mappingField) {
        System.out.println();
    }
}
