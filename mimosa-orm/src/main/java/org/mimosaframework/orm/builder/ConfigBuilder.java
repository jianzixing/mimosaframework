package org.mimosaframework.orm.builder;

import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.ContextException;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface ConfigBuilder {
    ApplicationSetting getApplication();

    CenterConfigSetting getCenterInfo() throws ContextException;

    List<MimosaDataSource> getDataSources() throws SQLException;

    Set<Class> getResolvers() throws ContextException;

    List<? extends IDStrategy> getStrategies();

    BasicSetting getBasicInfo() throws ContextException;

    List<String> getMappers();
}
