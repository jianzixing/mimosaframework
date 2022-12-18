package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.ColumnCompareType;
import org.mimosaframework.orm.platform.PlatformDialect;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.TableStructure;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class DB2PlatformDialect extends PlatformDialect {
    private DB2StampBuilder builder = new DB2StampBuilder();

    public DB2PlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INTEGER");
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.CHAR, "CHARACTER", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.TINYINT, "SMALLINT");
        registerColumnType(KeyColumnType.SMALLINT, "SMALLINT");
        registerColumnType(KeyColumnType.BIGINT, "BIGINT");
        registerColumnType(KeyColumnType.FLOAT, "REAL");
        registerColumnType(KeyColumnType.DOUBLE, "DOUBLE");
        registerColumnType(KeyColumnType.DECIMAL, "DECIMAL", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.BOOLEAN, "CHARACTER", 1, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "TIME");
        registerColumnType(KeyColumnType.DATETIME, "TIMESTAMP");
        registerColumnType(KeyColumnType.TIMESTAMP, "TIMESTAMP");

        registerColumnType(KeyColumnType.BLOB, "BLOB");
        registerColumnType(KeyColumnType.MEDIUMBLOB, "BLOB");
        registerColumnType(KeyColumnType.LONGBLOB, "BLOB");
        registerColumnType(KeyColumnType.TEXT, "CLOB");
        registerColumnType(KeyColumnType.JSON, "CLOB");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "CLOB");
        registerColumnType(KeyColumnType.LONGTEXT, "CLOB");
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
                // 如果无效使用这个 select current schema from sysibm.sysdummy1
                schema = resultSet.getString("TABLE_SCHEM");
                break;
            }
            resultSet.close();
            return schema;
        } finally {
            close(connection);
        }
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
                this.runner(alterBuilder.compile());
            }
        }
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
