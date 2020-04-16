package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class DB2PlatformDialect extends PlatformDialect {

    public DB2PlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INT");
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.CHAR, "CHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.TINYINT, "SMALLINT");
        registerColumnType(KeyColumnType.SMALLINT, "SMALLINT");
        registerColumnType(KeyColumnType.BIGINT, "BIGINT");
        registerColumnType(KeyColumnType.FLOAT, "REAL");
        registerColumnType(KeyColumnType.DOUBLE, "DOUBLE");
        registerColumnType(KeyColumnType.DECIMAL, "DECIMAL", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.BOOLEAN, "CHAR", 1, ColumnCompareType.SELF);
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

    protected String getCatalogAndSchema() throws SQLException {
        Connection connection = null;
        try {
            connection = dataSourceWrapper.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getSchemas();
            String schema = null;
            while (resultSet.next()) {
                // 取排名第一个的SCHEMA
                // 如果无效使用这个 select current schema from sysibm.sysdummy1
                schema = resultSet.getString("TABLE_SCHEM");
                break;
            }
            resultSet.close();
            return schema;
        } finally {
            dataSourceWrapper.close();
        }
    }

    @Override
    public SQLBuilderCombine alter(StampAlter alter) {
        StampCombineBuilder builder = new DB2StampAlter();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine create(StampCreate create) {
        StampCombineBuilder builder = new DB2StampCreate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, create);
        return combine;
    }

    @Override
    public SQLBuilderCombine drop(StampDrop drop) {
        StampCombineBuilder builder = new DB2StampDrop();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, drop);
        return combine;
    }

    @Override
    public SQLBuilderCombine insert(StampInsert insert) {
        StampCombineBuilder builder = new DB2StampInsert();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return combine;
    }

    @Override
    public SQLBuilderCombine delete(StampDelete delete) {
        StampCombineBuilder builder = new DB2StampDelete();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, delete);
        return combine;
    }

    @Override
    public SQLBuilderCombine select(StampSelect select) {
        StampCombineBuilder builder = new DB2StampSelect();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, select);
        return combine;
    }

    @Override
    public SQLBuilderCombine update(StampUpdate update) {
        StampCombineBuilder builder = new DB2StampUpdate();
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

                List<ColumnEditType> columnEditTypes = this.compareColumnChange(tableStructure, mappingField, columnStructure);
                if (columnEditTypes != null && columnEditTypes.size() > 0) {
                    DefaultSQLAlterBuilder sql = new DefaultSQLAlterBuilder();
                    sql.alter().table(tableName).modify().column(columnName);
                    boolean needRun = false;
                    if (columnEditTypes.indexOf(ColumnEditType.TYPE) >= 0
                            || columnEditTypes.indexOf(ColumnEditType.ISNULL) >= 0) {
                        // ColumnEditType.TYPE;
                        this.setSQLType(sql, mappingField.getMappingFieldType(),
                                mappingField.getMappingFieldLength(), mappingField.getMappingFieldDecimalDigits());

                        if (!mappingField.isMappingFieldNullable()) {
                            sql.not();
                            sql.nullable();
                        }
                        needRun = true;
                    }

                    if (columnEditTypes.indexOf(ColumnEditType.DEF_VALUE) >= 0) {
                        // ColumnEditType.DEF_VALUE;
                        String def = mappingField.getMappingFieldDefaultValue();
                        if (StringTools.isNotEmpty(def)) {
                            sql.defaultValue(def);
                            needRun = true;
                        }
                    }
                    if (columnEditTypes.indexOf(ColumnEditType.COMMENT) >= 0) {
                        String cmt = mappingField.getMappingFieldComment();
                        if (StringTools.isNotEmpty(cmt)) {
                            sql.comment(cmt);
                            needRun = true;
                        }
                    }
                    if (needRun) {
                        this.runner(sql.compile());
                    }

                    if (columnStructure != null) columnStructure.setState(1);
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

    }

    @Override
    public boolean isSupportGeneratedKeys() {
        return false;
    }
}
