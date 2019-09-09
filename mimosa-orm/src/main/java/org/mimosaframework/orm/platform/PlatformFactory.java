package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.MimosaDataSource;
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
        return null;
    }

    public static DatabasePorter getDatabasePorter(MimosaDataSource dataSource) {
        DatabaseTypeEnum typeEnum = dataSource.getDatabaseTypeEnum();
        if (typeEnum.equals(DatabaseTypeEnum.MYSQL)) {
            return new MysqlDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypeEnum.ORACLE)) {
            return new OracleDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypeEnum.SQL_SERVER)) {
            return new SQLServerDatabasePorter();
        }
        if (typeEnum.equals(DatabaseTypeEnum.POSTGRESQL)) {
            return new PostgreSQLDatabasePorter();
        }
        return null;
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
        return null;
    }

    public static PlatformWrapper getPlatformWrapper(ActionDataSourceWrapper dswrapper) {
        MimosaDataSource dataSource = dswrapper.getDataSource();

        CarryHandler carryHandler = getCarryHandler(dswrapper);
        DatabasePorter databasePorter = getDatabasePorter(dataSource);

        if (carryHandler == null || databasePorter == null) {
            throw new IllegalArgumentException("不支持的数据库平台");
        }

        return new PlatformWrapperImpl(databasePorter, carryHandler);
    }

    public static ObjectSymbolContrast getPlatformSymbolContrast(MimosaDataSource dataSource) {
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
        return new MysqlObjectSymbolContrast();
    }

    public static SimpleTemplate getPlatformSimpleSession(ActionDataSourceWrapper dswrapper) {
        MimosaDataSource dataSource = dswrapper.getDataSource();

        CarryHandler carryHandler = getCarryHandler(dswrapper);
        DatabasePorter databasePorter = getDatabasePorter(dataSource);

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
        return null;
    }
}
