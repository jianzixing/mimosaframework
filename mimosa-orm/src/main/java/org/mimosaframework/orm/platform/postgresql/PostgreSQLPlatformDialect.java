package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.create.CreateFactory;
import org.mimosaframework.orm.sql.drop.DropFactory;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLPlatformDialect extends PlatformDialect {
    public PostgreSQLPlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INT4");
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.CHAR, "BPCHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.TINYINT, "INT2");
        registerColumnType(KeyColumnType.SMALLINT, "INT2");
        registerColumnType(KeyColumnType.BIGINT, "INT8");
        registerColumnType(KeyColumnType.FLOAT, "FLOAT4");
        registerColumnType(KeyColumnType.DOUBLE, "FLOAT8");
        registerColumnType(KeyColumnType.DECIMAL, "NUMERIC", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.BOOLEAN, "BOOL");
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "TIME");
        registerColumnType(KeyColumnType.DATETIME, "TIMESTAMP");
        registerColumnType(KeyColumnType.TIMESTAMP, "TIMESTAMP");

        registerColumnType(KeyColumnType.BLOB, "BYTEA");
        registerColumnType(KeyColumnType.MEDIUMBLOB, "BYTEA");
        registerColumnType(KeyColumnType.LONGBLOB, "BYTEA");
        registerColumnType(KeyColumnType.TEXT, "TEXT");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "TEXT");
        registerColumnType(KeyColumnType.LONGTEXT, "TEXT");
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
                schema = resultSet.getString("TABLE_SCHEM");
                if (schema.equals("public")) break;
            }
            resultSet.close();
            return schema;
        } finally {
            dataSourceWrapper.close();
        }
    }

    @Override
    public SQLBuilderCombine alter(StampAlter alter) {
        StampCombineBuilder builder = new PostgreSQLStampAlter();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine create(StampCreate create) {
        StampCombineBuilder builder = new PostgreSQLStampCreate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, create);
        return combine;
    }

    @Override
    public SQLBuilderCombine drop(StampDrop drop) {
        StampCombineBuilder builder = new PostgreSQLStampDrop();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, drop);
        return combine;
    }

    @Override
    public SQLBuilderCombine insert(StampInsert insert) {
        StampCombineBuilder builder = new PostgreSQLStampInsert();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return combine;
    }

    @Override
    public SQLBuilderCombine delete(StampDelete delete) {
        StampCombineBuilder builder = new PostgreSQLStampDelete();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, delete);
        return combine;
    }

    @Override
    public SQLBuilderCombine select(StampSelect select) {
        StampCombineBuilder builder = new PostgreSQLStampSelect();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, select);
        return combine;
    }

    @Override
    public SQLBuilderCombine update(StampUpdate update) {
        StampCombineBuilder builder = new PostgreSQLStampUpdate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, update);
        return combine;
    }

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }

    @Override
    protected void createIndex(MappingTable mappingTable, MappingField mappingField, boolean unique) throws SQLException {
        String tableName = mappingTable.getMappingTableName();
        String indexName = "idx_" + mappingField.getMappingColumnName();
        StampAction stampAction = CreateFactory.create()
                .index().name(indexName).on().table(tableName)
                .columns(mappingField.getMappingColumnName()).compile();
        this.runner(stampAction);
    }

    @Override
    protected void dropIndex(MappingTable mappingTable, MappingField mappingField) throws SQLException {
        String tableName = mappingTable.getMappingTableName();
        String indexName = "idx_" + mappingField.getMappingColumnName();
        StampAction stampAction = DropFactory.drop().index()
                .name(indexName).on().table(tableName).compile();
        this.runner(stampAction);
    }
}
