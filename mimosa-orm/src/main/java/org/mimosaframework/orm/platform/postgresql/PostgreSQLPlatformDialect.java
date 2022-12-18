package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.orm.platform.ColumnCompareType;
import org.mimosaframework.orm.platform.PlatformDialect;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLPlatformDialect extends PlatformDialect {
    private PostgreSQLStampBuilder builder = new PostgreSQLStampBuilder();

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
        registerColumnType(KeyColumnType.JSON, "TEXT");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "TEXT");
        registerColumnType(KeyColumnType.LONGTEXT, "TEXT");
    }

    protected String getCatalogAndSchema() throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
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
            close(connection);
        }
    }

    @Override
    protected boolean compareColumnChangeDefault(String defA, String defB) {
        boolean last = false;
        if (defB != null) defB = defB.trim();
        if (defB != null && defB.startsWith("'")) {
            if (defB.startsWith("'" + defA)) {
                last = true;
            }
        }
        return last;
    }

    @Override
    public SQLBuilderCombine alter(StampAlter alter) {
        StampCombineBuilder builder = this.builder.alter();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine rename(StampRename alter) {
        StampCombineBuilder builder = this.builder.rename();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine create(StampCreate create) {
        StampCombineBuilder builder = this.builder.create();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, create);
        return combine;
    }

    @Override
    public SQLBuilderCombine drop(StampDrop drop) {
        StampCombineBuilder builder = this.builder.drop();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, drop);
        return combine;
    }

    @Override
    public SQLBuilderCombine insert(StampInsert insert) {
        StampCombineBuilder builder = this.builder.insert();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return combine;
    }

    @Override
    public SQLBuilderCombine delete(StampDelete delete) {
        StampCombineBuilder builder = this.builder.delete();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, delete);
        return combine;
    }

    @Override
    public SQLBuilderCombine select(StampSelect select) {
        StampCombineBuilder builder = this.builder.select();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, select);
        return combine;
    }

    @Override
    public SQLBuilderCombine update(StampUpdate update) {
        StampCombineBuilder builder = this.builder.update();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, update);
        return combine;
    }

    @Override
    public SQLBuilderCombine save(StampInsert insert) {
        return null;
    }

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }

    @Override
    public boolean isSupportDuplicateKeyUpdate() {
        return false;
    }
}
