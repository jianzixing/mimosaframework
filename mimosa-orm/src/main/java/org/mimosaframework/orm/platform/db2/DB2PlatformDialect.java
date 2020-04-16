package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    protected void rebuildStartTable(MappingTable mappingTable, String tableName) throws SQLException {
        if (mappingTable != null) {
            StampCreate create = this.commonCreateTable(mappingTable, tableName, true);
            this.runner(create);
        }
    }

    protected void rebuildEndTable(MappingTable mappingTable, TableStructure tableStructure) throws SQLException {
        Set<MappingField> mappingFields = mappingTable.getMappingFields();
        for (MappingField mappingField : mappingFields) {
            if (mappingField.isMappingAutoIncrement()) {
                DefaultSQLAlterBuilder alterBuilder = new DefaultSQLAlterBuilder();
                alterBuilder.alter().table(mappingTable.getMappingTableName())
                        .modify().column(mappingField.getMappingColumnName())
                        .autoIncrement();
            }
        }
    }

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }
}
