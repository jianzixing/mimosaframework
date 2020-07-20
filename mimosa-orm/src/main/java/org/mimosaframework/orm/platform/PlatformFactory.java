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
        PlatformStampBuilder builder = null;
        if (databaseTypes == DatabaseType.DB2) {
            builder = new DB2StampBuilder();
        } else if (databaseTypes == DatabaseType.MYSQL) {
            builder = new MysqlStampBuilder();
        } else if (databaseTypes == DatabaseType.ORACLE) { // 没有schema概念
            builder = new OracleStampBuilder();
        } else if (databaseTypes == DatabaseType.POSTGRESQL) {
            builder = new PostgreSQLStampBuilder();
        } else if (databaseTypes == DatabaseType.SQLITE) {
            builder = new SqliteStampBuilder();
        } else if (databaseTypes == DatabaseType.SQL_SERVER) {
            builder = new SQLServerStampBuilder();
        }

        if (builder != null) {
            if (stampAction instanceof StampAlter) return builder.alter();
            if (stampAction instanceof StampRename) return builder.rename();
            if (stampAction instanceof StampCreate) return builder.create();
            if (stampAction instanceof StampDelete) return builder.delete();
            if (stampAction instanceof StampDrop) return builder.drop();
            if (stampAction instanceof StampInsert) return builder.insert();
            if (stampAction instanceof StampSelect) return builder.select();
            if (stampAction instanceof StampUpdate) return builder.update();
            if (stampAction instanceof StampStructure) return builder.structure();
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
