package org.mimosaframework.orm.builder;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;
import org.mimosaframework.orm.strategy.StrategyConfig;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigBuilder {
    ApplicationSetting getApplication();

    CenterConfigSetting getCenterInfo() throws ContextException;

    MimosaDataSource getDefaultDataSource();

    List<MimosaDataSource> getDataSourceList() throws SQLException;

    Set<Class> getResolvers() throws ContextException;

    Map<String, StrategyConfig> getStrategyConfig();

    BasicSetting getBasicInfo() throws ContextException;

    List<String> getMappers();

    List<FactoryBuilder> getAuxFactoryBuilder() throws ClassNotFoundException, IllegalAccessException, InstantiationException;
}
