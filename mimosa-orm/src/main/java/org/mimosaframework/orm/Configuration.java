package org.mimosaframework.orm;

import org.mimosaframework.orm.convert.NamingConvert;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SessionContext;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.transaction.TransactionFactory;
import org.mimosaframework.orm.utils.DatabaseType;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Configuration {
    List<? extends IDStrategy> getIdStrategies();

    MimosaDataSource getDataSourceByName(String dataSourceName);

    MappingGlobalWrapper getMappingGlobalWrapper();

    SessionContext newSessionContext(String dataSourceName, boolean supportTrans) throws SQLException;

    ModelObjectConvertKey getModelObjectConvertKey();

    NamingConvert getConvert();

    SQLDefinedLoader getDefinedLoader();

    MimosaDataSource getDefaultDataSource();

    MimosaDataSource getAnyDataSource();

    MappingLevel getMappingLevel();

    Set<Class> getResolvers();

    String getTablePrefix();

    MappingTable getMappingTableByClass(Class tableClass);

    MappingTable getMappingTableByClassName(String tableClassName);

    Map<String, MimosaDataSource> getGlobalDataSource();

    Set<MappingTable> getMappingTables();

    boolean isIgnoreEmptySlave();

    boolean isShowSQL();

    DatabaseType getDatabaseType();

    void clearMimosaDataSources();

    TransactionFactory getTransactionFactory();

    Session buildSession() throws SQLException;
}
