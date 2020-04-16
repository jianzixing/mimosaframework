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
    public static StampCombineBuilder getStampAlterBuilder(DatabaseType databaseTypes, StampAction stampAction) {
        if (databaseTypes == DatabaseType.DB2) {
            if (stampAction instanceof StampAlter) return new DB2StampAlter();
            if (stampAction instanceof StampCreate) return new DB2StampCreate();
            if (stampAction instanceof StampDelete) return new DB2StampDelete();
            if (stampAction instanceof StampDrop) return new DB2StampDrop();
            if (stampAction instanceof StampInsert) return new DB2StampInsert();
            if (stampAction instanceof StampSelect) return new DB2StampSelect();
            if (stampAction instanceof StampUpdate) return new DB2StampUpdate();
            if (stampAction instanceof StampStructure) return new DB2StampStructure();
        } else if (databaseTypes == DatabaseType.MYSQL) {
            if (stampAction instanceof StampAlter) return new MysqlStampAlter();
            if (stampAction instanceof StampCreate) return new MysqlStampCreate();
            if (stampAction instanceof StampDelete) return new MysqlStampDelete();
            if (stampAction instanceof StampDrop) return new MysqlStampDrop();
            if (stampAction instanceof StampInsert) return new MysqlStampInsert();
            if (stampAction instanceof StampSelect) return new MysqlStampSelect();
            if (stampAction instanceof StampUpdate) return new MysqlStampUpdate();
            if (stampAction instanceof StampStructure) return new MysqlStampStructure();
        } else if (databaseTypes == DatabaseType.ORACLE) { // 没有schema概念
            if (stampAction instanceof StampAlter) return new OracleStampAlter();
            if (stampAction instanceof StampCreate) return new OracleStampCreate();
            if (stampAction instanceof StampDelete) return new OracleStampDelete();
            if (stampAction instanceof StampDrop) return new OracleStampDrop();
            if (stampAction instanceof StampInsert) return new OracleStampInsert();
            if (stampAction instanceof StampSelect) return new OracleStampSelect();
            if (stampAction instanceof StampUpdate) return new OracleStampUpdate();
            if (stampAction instanceof StampStructure) return new OracleStampStructure();
        } else if (databaseTypes == DatabaseType.POSTGRESQL) {
            if (stampAction instanceof StampAlter) return new PostgreSQLStampAlter();
            if (stampAction instanceof StampCreate) return new PostgreSQLStampCreate();
            if (stampAction instanceof StampDelete) return new PostgreSQLStampDelete();
            if (stampAction instanceof StampDrop) return new PostgreSQLStampDrop();
            if (stampAction instanceof StampInsert) return new PostgreSQLStampInsert();
            if (stampAction instanceof StampSelect) return new PostgreSQLStampSelect();
            if (stampAction instanceof StampUpdate) return new PostgreSQLStampUpdate();
            if (stampAction instanceof StampStructure) return new PostgreSQLStampStructure();
        } else if (databaseTypes == DatabaseType.SQLITE) {
            if (stampAction instanceof StampAlter) return new SqliteStampAlter();
            if (stampAction instanceof StampCreate) return new SqliteStampCreate();
            if (stampAction instanceof StampDelete) return new SqliteStampDelete();
            if (stampAction instanceof StampDrop) return new SqliteStampDrop();
            if (stampAction instanceof StampInsert) return new SqliteStampInsert();
            if (stampAction instanceof StampSelect) return new SqliteStampSelect();
            if (stampAction instanceof StampUpdate) return new SqliteStampUpdate();
            if (stampAction instanceof StampStructure) return new SqliteStampStructure();
        } else if (databaseTypes == DatabaseType.SQL_SERVER) {
            if (stampAction instanceof StampAlter) return new SQLServerStampAlter();
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
            PlatformDialect dialect = null;
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.SQL_SERVER)) {
            PlatformDialect dialect = null;
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.POSTGRESQL)) {
            PlatformDialect dialect = null;
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.DB2)) {
            PlatformDialect dialect = new DB2PlatformDialect();
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        if (dataSourceWrapper.getDatabaseTypeEnum().equals(DatabaseType.SQLITE)) {
            PlatformDialect dialect = null;
            dialect.setDataSourceWrapper(dataSourceWrapper);
            return dialect;
        }
        return null;
    }
}
