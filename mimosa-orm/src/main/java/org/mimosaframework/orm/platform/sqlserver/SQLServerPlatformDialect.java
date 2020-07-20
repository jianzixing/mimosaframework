package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.create.CreateFactory;
import org.mimosaframework.orm.sql.drop.DropFactory;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;

public class SQLServerPlatformDialect extends PlatformDialect {
    private SQLServerStampBuilder builder = new SQLServerStampBuilder();

    public SQLServerPlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INT");
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.CHAR, "CHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.TINYINT, "SMALLINT");
        registerColumnType(KeyColumnType.SMALLINT, "SMALLINT");
        registerColumnType(KeyColumnType.BIGINT, "BIGINT");
        registerColumnType(KeyColumnType.FLOAT, "REAL");
        registerColumnType(KeyColumnType.DOUBLE, "REAL");
        registerColumnType(KeyColumnType.DECIMAL, "NUMERIC", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.BOOLEAN, "BIT");
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "TIME");
        registerColumnType(KeyColumnType.DATETIME, "DATETIME");
        registerColumnType(KeyColumnType.TIMESTAMP, "TIMESTAMP");

        registerColumnType(KeyColumnType.BLOB, "Binary");
        registerColumnType(KeyColumnType.MEDIUMBLOB, "VarBinary(max)");
        registerColumnType(KeyColumnType.LONGBLOB, "VarBinary(max)");
        registerColumnType(KeyColumnType.TEXT, "NTEXT");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "NTEXT");
        registerColumnType(KeyColumnType.LONGTEXT, "NTEXT");
    }

    protected boolean compareColumnChangeType(TableColumnStructure columnStructure, ColumnType columnType) {
        if (columnStructure.getTypeName().equalsIgnoreCase("VarBinary")
                && columnType.getTypeName().startsWith("VarBinary")) {
            return true;
        }
        return columnStructure.getTypeName().equalsIgnoreCase(columnType.getTypeName());
    }

    @Override
    protected boolean compareColumnChangeDefault(String defA, String defB) {
        boolean last = false;
        if (defB != null) defB = defB.trim();
        if (defB != null && defB.startsWith("('") && defB.endsWith("')")) {
            defB = defB.substring(2, defB.length() - 2);
            if (defB.equals(defA)) {
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
    protected DialectNextStep defineAddColumn(DataDefinition definition) throws SQLException {
        return super.defineAddColumn(definition);
    }

    protected void defineAddColumnDefaultNull(String tableName, String columnName) throws SQLException {

    }

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }

    @Override
    public boolean isSelectLimitMustOrderBy() {
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
