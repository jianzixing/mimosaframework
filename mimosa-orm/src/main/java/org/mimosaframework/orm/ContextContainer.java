package org.mimosaframework.orm;

import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.utils.DatabaseTypeEnum;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface ContextContainer {
    List<? extends IDStrategy> getIdStrategies();

    MimosaDataSource getDataSourceByName(String dataSourceName);

    MappingGlobalWrapper getMappingGlobalWrapper();

    ActionDataSourceWrapper getDefaultDataSourceWrapper(boolean isCreateNew);

    ModelObjectConvertKey getModelObjectConvertKey();

    MappingNamedConvert getConvert();

    SQLDefinedLoader getDefinedLoader();

    MimosaDataSource getDefaultDataSource();

    MimosaDataSource getAnyDataSource();

    List<FactoryBuilder> getAuxFactoryBuilder();

    MappingLevel getMappingLevel();

    Set<Class> getResolvers();

    MappingTable getMappingTableByClass(Class tableClass);

    MappingTable getMappingTableByClassName(String tableClassName);

    List<MimosaDataSource> getGlobalDataSource();

    Set<MappingTable> getMappingTables();

    boolean isIgnoreEmptySlave();

    boolean isShowSQL();

    void matchWholeMappingDatabase() throws SQLException;

    DatabaseTypeEnum getDatabaseType();

    void clearMimosaDataSources();
}
