package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.platform.db2.*;
import org.mimosaframework.orm.platform.mysql.*;
import org.mimosaframework.orm.platform.oracle.*;
import org.mimosaframework.orm.platform.postgresql.*;
import org.mimosaframework.orm.platform.sqlite.*;
import org.mimosaframework.orm.platform.sqlserver.*;
import org.mimosaframework.orm.sql.stamp.*;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class PlatformFactory {
    public static DifferentColumn getDifferentColumn(MimosaDataSource dataSource) {
        DatabaseTypes typeEnum = dataSource.getDatabaseTypeEnum();
        if (typeEnum.equals(DatabaseTypes.MYSQL)) {
            return new MysqlDifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypes.ORACLE)) {
            return new OracleDifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypes.SQL_SERVER)) {
            return new SQLServerDifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypes.POSTGRESQL)) {
            return new PostgreSQLDifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypes.DB2)) {
            return new DB2DifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypes.SQLITE)) {
            return new SqliteDifferentColumn();
        }
        return null;
    }

    public static DatabasePorter getDatabasePorter(ActionDataSourceWrapper dswrapper) {
        MimosaDataSource dataSource = dswrapper.getDataSource();
        DatabaseTypes typeEnum = dataSource.getDatabaseTypeEnum();
        DatabasePorter databasePorter = null;
        if (typeEnum.equals(DatabaseTypes.MYSQL)) {
            databasePorter = new MysqlDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypes.ORACLE)) {
            databasePorter = new OracleDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypes.SQL_SERVER)) {
            databasePorter = new SQLServerDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypes.POSTGRESQL)) {
            databasePorter = new PostgreSQLDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypes.DB2)) {
            databasePorter = new DB2DatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypes.SQLITE)) {
            databasePorter = new SqliteDatabasePorter();
        }
        if (databasePorter != null) {
            databasePorter.setCarryHandler(getCarryHandler(dswrapper));
        }
        return databasePorter;
    }

    public static CarryHandler getCarryHandler(ActionDataSourceWrapper dswrapper) {
        MimosaDataSource dataSource = dswrapper.getDataSource();
        DatabaseTypes typeEnum = dataSource.getDatabaseTypeEnum();
        if (typeEnum.equals(DatabaseTypes.MYSQL)) {
            return new MysqlCarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypes.ORACLE)) {
            return new OracleCarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypes.SQL_SERVER)) {
            return new SQLServerCarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypes.POSTGRESQL)) {
            return new PostgreSQLCarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypes.DB2)) {
            return new DB2CarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypes.SQLITE)) {
            return new SqliteCarryHandler(dswrapper);
        }
        return null;
    }

    public static PlatformWrapper getPlatformWrapper(ActionDataSourceWrapper dswrapper) {
        CarryHandler carryHandler = getCarryHandler(dswrapper);
        DatabasePorter databasePorter = getDatabasePorter(dswrapper);

        if (carryHandler == null || databasePorter == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    PlatformFactory.class, "not_support_platform"));
        }

        return new PlatformWrapperImpl(databasePorter, carryHandler);
    }

    public static ObjectSymbolContrast getSymbolContrast(MimosaDataSource dataSource) {
        return getSymbolContrast(dataSource.getDatabaseTypeEnum());
    }

    public static ObjectSymbolContrast getSymbolContrast(DatabaseTypes type) {
        if (type.equals(DatabaseTypes.MYSQL)) {
            return new MysqlObjectSymbolContrast();
        }
        if (type.equals(DatabaseTypes.ORACLE)) {
            return new OracleObjectSymbolContrast();
        }
        if (type.equals(DatabaseTypes.SQL_SERVER)) {
            return new SQLServerObjectSymbolContrast();
        }
        if (type.equals(DatabaseTypes.POSTGRESQL)) {
            return new PostgreSQLObjectSymbolContrast();
        }
        if (type.equals(DatabaseTypes.DB2)) {
            return new DB2ObjectSymbolContrast();
        }
        if (type.equals(DatabaseTypes.SQLITE)) {
            return new SqliteObjectSymbolContrast();
        }
        return null;
    }

    public static DatabaseSpeciality getLocalSpeciality(MimosaDataSource dataSource) {
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypes.MYSQL)) {
            return new MysqlDatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypes.ORACLE)) {
            return new OracleDatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypes.SQL_SERVER)) {
            return new SQLServerDatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypes.POSTGRESQL)) {
            return new PostgreSQLDatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypes.DB2)) {
            return new DB2DatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypes.SQLITE)) {
            return new SqliteDatabaseSpeciality();
        }
        return null;
    }

    public static StampCombineBuilder getStampAlterBuilder(DatabaseTypes databaseTypes, StampAction stampAction) {
        if (databaseTypes == DatabaseTypes.DB2) {
            if (stampAction instanceof StampAlter) return new DB2StampAlter();
            if (stampAction instanceof StampCreate) return new DB2StampCreate();
            if (stampAction instanceof StampDelete) return new DB2StampDelete();
            if (stampAction instanceof StampDrop) return new DB2StampDrop();
            if (stampAction instanceof StampInsert) return new DB2StampInsert();
            if (stampAction instanceof StampSelect) return new DB2StampSelect();
            if (stampAction instanceof StampUpdate) return new DB2StampUpdate();
            if (stampAction instanceof StampStructure) return new DB2StampStructure();
        } else if (databaseTypes == DatabaseTypes.MYSQL) {
            if (stampAction instanceof StampAlter) return new MysqlStampAlter();
            if (stampAction instanceof StampCreate) return new MysqlStampCreate();
            if (stampAction instanceof StampDelete) return new MysqlStampDelete();
            if (stampAction instanceof StampDrop) return new MysqlStampDrop();
            if (stampAction instanceof StampInsert) return new MysqlStampInsert();
            if (stampAction instanceof StampSelect) return new MysqlStampSelect();
            if (stampAction instanceof StampUpdate) return new MysqlStampUpdate();
            if (stampAction instanceof StampStructure) return new MysqlStampStructure();
        } else if (databaseTypes == DatabaseTypes.ORACLE) { // 没有schema概念
            if (stampAction instanceof StampAlter) return new OracleStampAlter();
            if (stampAction instanceof StampCreate) return new OracleStampCreate();
            if (stampAction instanceof StampDelete) return new OracleStampDelete();
            if (stampAction instanceof StampDrop) return new OracleStampDrop();
            if (stampAction instanceof StampInsert) return new OracleStampInsert();
            if (stampAction instanceof StampSelect) return new OracleStampSelect();
            if (stampAction instanceof StampUpdate) return new OracleStampUpdate();
            if (stampAction instanceof StampStructure) return new OracleStampStructure();
        } else if (databaseTypes == DatabaseTypes.POSTGRESQL) {
            if (stampAction instanceof StampAlter) return new PostgreSQLStampAlter();
            if (stampAction instanceof StampCreate) return new PostgreSQLStampCreate();
            if (stampAction instanceof StampDelete) return new PostgreSQLStampDelete();
            if (stampAction instanceof StampDrop) return new PostgreSQLStampDrop();
            if (stampAction instanceof StampInsert) return new PostgreSQLStampInsert();
            if (stampAction instanceof StampSelect) return new PostgreSQLStampSelect();
            if (stampAction instanceof StampUpdate) return new PostgreSQLStampUpdate();
            if (stampAction instanceof StampStructure) return new PostgreSQLStampStructure();
        } else if (databaseTypes == DatabaseTypes.SQLITE) {
            if (stampAction instanceof StampAlter) return new SqliteStampAlter();
            if (stampAction instanceof StampCreate) return new SqliteStampCreate();
            if (stampAction instanceof StampDelete) return new SqliteStampDelete();
            if (stampAction instanceof StampDrop) return new SqliteStampDrop();
            if (stampAction instanceof StampInsert) return new SqliteStampInsert();
            if (stampAction instanceof StampSelect) return new SqliteStampSelect();
            if (stampAction instanceof StampUpdate) return new SqliteStampUpdate();
            if (stampAction instanceof StampStructure) return new SqliteStampStructure();
        } else if (databaseTypes == DatabaseTypes.SQL_SERVER) {
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

    public static PlatformDialect getDialect(DatabaseTypes databaseTypes) {
        if (databaseTypes.equals(DatabaseTypes.MYSQL)) {
        }
        if (databaseTypes.equals(DatabaseTypes.ORACLE)) {
        }
        if (databaseTypes.equals(DatabaseTypes.SQL_SERVER)) {
        }
        if (databaseTypes.equals(DatabaseTypes.POSTGRESQL)) {
        }
        if (databaseTypes.equals(DatabaseTypes.DB2)) {
        }
        if (databaseTypes.equals(DatabaseTypes.SQLITE)) {
        }
        return null;
    }
}
