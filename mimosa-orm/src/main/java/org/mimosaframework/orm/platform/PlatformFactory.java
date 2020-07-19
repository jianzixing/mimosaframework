package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.platform.db2.*;
import org.mimosaframework.orm.platform.mysql.*;
import org.mimosaframework.orm.platform.oracle.*;
import org.mimosaframework.orm.platform.postgresql.*;
import org.mimosaframework.orm.platform.sqlite.*;
import org.mimosaframework.orm.platform.sqlserver.*;
import org.mimosaframework.orm.sql.stamp.*;
import org.mimosaframework.orm.utils.DatabaseType;

public class PlatformFactory {
    public static StampCombineBuilder getStampBuilder(DatabaseType databaseTypes, StampAction stampAction) {
        if (databaseTypes == DatabaseType.DB2) {
            DB2StampBuilder builder = new DB2StampBuilder();
            if (stampAction instanceof StampAlter) return builder.alter();
            if (stampAction instanceof StampRename) return builder.rename();
            if (stampAction instanceof StampCreate) return builder.create();
            if (stampAction instanceof StampDelete) return builder.delete();
            if (stampAction instanceof StampDrop) return builder.drop();
            if (stampAction instanceof StampInsert) return builder.insert();
            if (stampAction instanceof StampSelect) return builder.select();
            if (stampAction instanceof StampUpdate) return builder.update();
            if (stampAction instanceof StampStructure) return builder.structure();
        } else if (databaseTypes == DatabaseType.MYSQL) {
            if (stampAction instanceof StampAlter) return new MysqlStampAlter();
            if (stampAction instanceof StampRename) return new MysqlStampRename();
            if (stampAction instanceof StampCreate) return new MysqlStampCreate();
            if (stampAction instanceof StampDelete) return new MysqlStampDelete();
            if (stampAction instanceof StampDrop) return new MysqlStampDrop();
            if (stampAction instanceof StampInsert) return new MysqlStampInsert();
            if (stampAction instanceof StampSelect) return new MysqlStampSelect();
            if (stampAction instanceof StampUpdate) return new MysqlStampUpdate();
            if (stampAction instanceof StampStructure) return new MysqlStampStructure();
        } else if (databaseTypes == DatabaseType.ORACLE) { // 没有schema概念
            if (stampAction instanceof StampAlter) return new OracleStampAlter();
            if (stampAction instanceof StampRename) return new OracleStampRename();
            if (stampAction instanceof StampCreate) return new OracleStampCreate();
            if (stampAction instanceof StampDelete) return new OracleStampDelete();
            if (stampAction instanceof StampDrop) return new OracleStampDrop();
            if (stampAction instanceof StampInsert) return new OracleStampInsert();
            if (stampAction instanceof StampSelect) return new OracleStampSelect();
            if (stampAction instanceof StampUpdate) return new OracleStampUpdate();
            if (stampAction instanceof StampStructure) return new OracleStampStructure();
        } else if (databaseTypes == DatabaseType.POSTGRESQL) {
            if (stampAction instanceof StampAlter) return new PostgreSQLStampAlter();
            if (stampAction instanceof StampRename) return new PostgreSQLStampRename();
            if (stampAction instanceof StampCreate) return new PostgreSQLStampCreate();
            if (stampAction instanceof StampDelete) return new PostgreSQLStampDelete();
            if (stampAction instanceof StampDrop) return new PostgreSQLStampDrop();
            if (stampAction instanceof StampInsert) return new PostgreSQLStampInsert();
            if (stampAction instanceof StampSelect) return new PostgreSQLStampSelect();
            if (stampAction instanceof StampUpdate) return new PostgreSQLStampUpdate();
            if (stampAction instanceof StampStructure) return new PostgreSQLStampStructure();
        } else if (databaseTypes == DatabaseType.SQLITE) {
            if (stampAction instanceof StampAlter) return new SqliteStampAlter();
            if (stampAction instanceof StampRename) return new SqliteStampRename();
            if (stampAction instanceof StampCreate) return new SqliteStampCreate();
            if (stampAction instanceof StampDelete) return new SqliteStampDelete();
            if (stampAction instanceof StampDrop) return new SqliteStampDrop();
            if (stampAction instanceof StampInsert) return new SqliteStampInsert();
            if (stampAction instanceof StampSelect) return new SqliteStampSelect();
            if (stampAction instanceof StampUpdate) return new SqliteStampUpdate();
            if (stampAction instanceof StampStructure) return new SqliteStampStructure();
        } else if (databaseTypes == DatabaseType.SQL_SERVER) {
            if (stampAction instanceof StampAlter) return new SQLServerStampAlter();
            if (stampAction instanceof StampRename) return new SQLServerStampRename();
            if (stampAction instanceof StampCreate) return new SQLServerStampCreate();
            if (stampAction instanceof StampDelete) return new SQLServerStampDelete();
            if (stampAction instanceof StampDrop) return new SQLServerStampDrop();
            if (stampAction instanceof StampInsert) return new SQLServerStampInsert();
            if (stampAction instanceof StampSelect) return new SQLServerStampSelect();
            if (stampAction instanceof StampUpdate) return new SQLServerStampUpdate();
            if (stampAction instanceof StampStructure) return new SQLServerStampStructure();
        }
        return null;
    }

    public static PlatformDialect getDialect(DataSourceWrapper dataSourceWrapper) {
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.MYSQL)) {
            PlatformDialect dialect = new MysqlPlatformDialect();
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.ORACLE)) {
            PlatformDialect dialect = new OraclePlatformDialect();
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.SQL_SERVER)) {
            PlatformDialect dialect = new SQLServerPlatformDialect();
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.POSTGRESQL)) {
            PlatformDialect dialect = new PostgreSQLPlatformDialect();
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.DB2)) {
            PlatformDialect dialect = new DB2PlatformDialect();
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.SQLITE)) {
            PlatformDialect dialect = new SqlitePlatformDialect();
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        return null;
    }
}
