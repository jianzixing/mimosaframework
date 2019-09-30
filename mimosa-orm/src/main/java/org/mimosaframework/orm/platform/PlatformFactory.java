package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.db2.*;
import org.mimosaframework.orm.platform.mysql.*;
import org.mimosaframework.orm.platform.oracle.*;
import org.mimosaframework.orm.platform.postgresql.*;
import org.mimosaframework.orm.platform.sqlserver.*;
import org.mimosaframework.orm.utils.DatabaseTypeEnum;

public class PlatformFactory {
    public static DifferentColumn getDifferentColumn(MimosaDataSource dataSource) {
        DatabaseTypeEnum typeEnum = dataSource.getDatabaseTypeEnum();
        if (typeEnum.equals(DatabaseTypeEnum.MYSQL)) {
            return new MysqlDifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypeEnum.ORACLE)) {
            return new OracleDifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypeEnum.SQL_SERVER)) {
            return new SQLServerDifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypeEnum.POSTGRESQL)) {
            return new PostgreSQLDifferentColumn();
        }
        if (typeEnum.equals(DatabaseTypeEnum.DB2)) {
            return new DB2DifferentColumn();
        }
        return null;
    }

    public static DatabasePorter getDatabasePorter(ActionDataSourceWrapper dswrapper) {
        MimosaDataSource dataSource = dswrapper.getDataSource();
        DatabaseTypeEnum typeEnum = dataSource.getDatabaseTypeEnum();
        DatabasePorter databasePorter = null;
        if (typeEnum.equals(DatabaseTypeEnum.MYSQL)) {
            databasePorter = new MysqlDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypeEnum.ORACLE)) {
            databasePorter = new OracleDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypeEnum.SQL_SERVER)) {
            databasePorter = new SQLServerDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypeEnum.POSTGRESQL)) {
            databasePorter = new PostgreSQLDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypeEnum.DB2)) {
            databasePorter = new DB2DatabasePorter();
        }
        if (databasePorter != null) {
            databasePorter.setCarryHandler(getCarryHandler(dswrapper));
        }
        return databasePorter;
    }

    public static CarryHandler getCarryHandler(ActionDataSourceWrapper dswrapper) {
        MimosaDataSource dataSource = dswrapper.getDataSource();
        DatabaseTypeEnum typeEnum = dataSource.getDatabaseTypeEnum();
        if (typeEnum.equals(DatabaseTypeEnum.MYSQL)) {
            return new MysqlCarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypeEnum.ORACLE)) {
            return new OracleCarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypeEnum.SQL_SERVER)) {
            return new SQLServerCarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypeEnum.POSTGRESQL)) {
            return new PostgreSQLCarryHandler(dswrapper);
        }
        if (typeEnum.equals(DatabaseTypeEnum.DB2)) {
            return new DB2CarryHandler(dswrapper);
        }
        return null;
    }

    public static PlatformWrapper getPlatformWrapper(ActionDataSourceWrapper dswrapper) {
        CarryHandler carryHandler = getCarryHandler(dswrapper);
        DatabasePorter databasePorter = getDatabasePorter(dswrapper);

        if (carryHandler == null || databasePorter == null) {
            throw new IllegalArgumentException("不支持的数据库平台");
        }

        return new PlatformWrapperImpl(databasePorter, carryHandler);
    }

    public static ObjectSymbolContrast getSymbolContrast(MimosaDataSource dataSource) {
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.MYSQL)) {
            return new MysqlObjectSymbolContrast();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.ORACLE)) {
            return new OracleObjectSymbolContrast();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.SQL_SERVER)) {
            return new SQLServerObjectSymbolContrast();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.POSTGRESQL)) {
            return new PostgreSQLObjectSymbolContrast();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.DB2)) {
            return new DB2ObjectSymbolContrast();
        }
        return null;
    }

    public static SimpleTemplate getPlatformSimpleSession(ActionDataSourceWrapper dswrapper) {
        CarryHandler carryHandler = getCarryHandler(dswrapper);
        DatabasePorter databasePorter = getDatabasePorter(dswrapper);

        if (carryHandler == null || databasePorter == null) {
            throw new IllegalArgumentException("不支持的数据库平台");
        }

        return new SimpleTemplateImpl(databasePorter, carryHandler);
    }

    public static DatabaseSpeciality getLocalSpeciality(MimosaDataSource dataSource) {
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.MYSQL)) {
            return new MysqlDatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.ORACLE)) {
            return new OracleDatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.SQL_SERVER)) {
            return new SQLServerDatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.POSTGRESQL)) {
            return new PostgreSQLDatabaseSpeciality();
        }
        if (dataSource.getDatabaseTypeEnum().equals(DatabaseTypeEnum.DB2)) {
            return new DB2DatabaseSpeciality();
        }
        return null;
    }
}
