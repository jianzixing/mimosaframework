package org.mimosaframework.orm;

import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.convert.NamingConvert;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.DataSourceWrapper;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.utils.DatabaseType;

import java.util.List;
import java.util.Set;

public interface ContextContainer {
    List<? extends IDStrategy> getIdStrategies();

    MimosaDataSource getDataSourceByName(String dataSourceName);

    MappingGlobalWrapper getMappingGlobalWrapper();

    DataSourceWrapper getDefaultDataSourceWrapper(boolean isCreateNew);

    ModelObjectConvertKey getModelObjectConvertKey();

    NamingConvert getConvert();

    SQLDefinedLoader getDefinedLoader();

    MimosaDataSource getDefaultDataSource();

    MimosaDataSource getAnyDataSource();

    List<FactoryBuilder> getAuxFactoryBuilder();

    MappingLevel getMappingLevel();

    Set<Class> getResolvers();

    String getTablePrefix();

    MappingTable getMappingTableByClass(Class tableClass);

    MappingTable getMappingTableByClassName(String tableClassName);

    List<MimosaDataSource> getGlobalDataSource();

    Set<MappingTable> getMappingTables();

    boolean isIgnoreEmptySlave();

    boolean isShowSQL();

    DatabaseType getDatabaseType();

    void clearMimosaDataSources();

    AbstractInterceptSession getInterceptSession();

    Session buildSession();
}
