package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.platform.db2.DB2PlatformDialect;
import org.mimosaframework.orm.platform.db2.DB2StampBuilder;
import org.mimosaframework.orm.platform.mysql.MysqlPlatformDialect;
import org.mimosaframework.orm.platform.mysql.MysqlStampBuilder;
import org.mimosaframework.orm.platform.oracle.OraclePlatformDialect;
import org.mimosaframework.orm.platform.oracle.OracleStampBuilder;
import org.mimosaframework.orm.platform.postgresql.PostgreSQLPlatformDialect;
import org.mimosaframework.orm.platform.postgresql.PostgreSQLStampBuilder;
import org.mimosaframework.orm.platform.sqlite.SqlitePlatformDialect;
import org.mimosaframework.orm.platform.sqlite.SqliteStampBuilder;
import org.mimosaframework.orm.platform.sqlserver.SQLServerPlatformDialect;
import org.mimosaframework.orm.platform.sqlserver.SQLServerStampBuilder;
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

    public static PlatformDialect getDialect(SessionContext sessionContext) {
        if (sessionContext.getDatabaseTypeEnum().equals(DatabaseType.MYSQL)) {
            PlatformDialect dialect = new MysqlPlatformDialect();
            dialect.setSessionContext(sessionContext);
            return dialect;
        }
        if (sessionContext.getDatabaseTypeEnum().equals(DatabaseType.ORACLE)) {
            PlatformDialect dialect = new OraclePlatformDialect();
            dialect.setSessionContext(sessionContext);
            return dialect;
        }
        if (sessionContext.getDatabaseTypeEnum().equals(DatabaseType.SQL_SERVER)) {
            PlatformDialect dialect = new SQLServerPlatformDialect();
            dialect.setSessionContext(sessionContext);
            return dialect;
        }
        if (sessionContext.getDatabaseTypeEnum().equals(DatabaseType.POSTGRESQL)) {
            PlatformDialect dialect = new PostgreSQLPlatformDialect();
            dialect.setSessionContext(sessionContext);
            return dialect;
        }
        if (sessionContext.getDatabaseTypeEnum().equals(DatabaseType.DB2)) {
            PlatformDialect dialect = new DB2PlatformDialect();
            dialect.setSessionContext(sessionContext);
            return dialect;
        }
        if (sessionContext.getDatabaseTypeEnum().equals(DatabaseType.SQLITE)) {
            PlatformDialect dialect = new SqlitePlatformDialect();
            dialect.setSessionContext(sessionContext);
            return dialect;
        }
        return null;
    }
}
