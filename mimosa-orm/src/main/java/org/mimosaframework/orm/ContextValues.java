package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObjectChecker;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.convert.ConvertFactory;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;
import org.mimosaframework.orm.scripting.DefinerConfigure;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.strategy.StrategyConfig;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextValues {
    private ModelObjectConvertKey modelObjectConvertKey = new SimpleModelObjectConvertKey(ConvertFactory.getDefaultConvert());
    private Map<String, MimosaDataSource> globalDataSource = new LinkedHashMap<>();
    private List<FactoryBuilder> factoryBuilderList = new CopyOnWriteArrayList<>();

    private String applicationName;
    private String applicationDetail;
    private MappingLevel mappingLevel;

    private Set<Class> resolvers;
    private MappingGlobalWrapper mappingGlobalWrapper;
    private MappingNamedConvert convert;
    private ModelObjectChecker checker;
    private ActionDataSourceWrapper defaultDataSource;

    /**
     * 如果使用了分表策略是数据库自增ID则配置会存在当前里面
     * key的配置是表的类名+字段名如果没有配置字段名称则表示当前类所有的
     * 字段都可以使用
     */
    private Map<String, StrategyConfig> strategyDataSource;
    private List<? extends IDStrategy> idStrategies;
    private boolean isShowSQL = false;
    private List<String> mappers;
    private SQLDefinedLoader definedLoader;

    /**
     * True : 在选择从库时如果没有从库则选择主库,防止报错
     * False : 在选择从库时如果没有从库返回为空直接报错
     */
    private boolean isIgnoreEmptySlave = true;

    private Context context;

    public MappingLevel getMappingLevel() {
        if (mappingLevel == null) {
            mappingLevel = MappingLevel.CREATE;
        }
        return mappingLevel;
    }

    public void setMappingLevel(MappingLevel mappingLevel) {
        this.mappingLevel = mappingLevel;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDetail() {
        return applicationDetail;
    }

    public void setApplicationDetail(String applicationDetail) {
        this.applicationDetail = applicationDetail;
    }

    public Set<Class> getResolvers() {
        return resolvers;
    }

    public void setResolvers(Set<Class> resolvers) {
        this.resolvers = resolvers;
    }

    public MappingNamedConvert getConvert() {
        return convert;
    }

    public void setConvert(MappingNamedConvert convert) {
        this.convert = convert;
        modelObjectConvertKey.setMappingNamedConvert(convert);
    }

    public ModelObjectChecker getChecker() {
        return checker;
    }

    public void setChecker(ModelObjectChecker checker) {
        this.checker = checker;
    }

    public ActionDataSourceWrapper getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(ActionDataSourceWrapper defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
        MimosaDataSource ds = defaultDataSource.getDataSource();
        if (ds == null) {
            throw new IllegalArgumentException("必须设置一个默认的数据源");
        }
        if (StringTools.isEmpty(ds.getName())) {
            throw new IllegalArgumentException("默认数据源必须设置一个名称");
        }
        this.globalDataSource.put(ds.getName(), ds);
    }

    public Map<String, StrategyConfig> getStrategyDataSource() {
        return strategyDataSource;
    }

    public void setStrategyDataSource(Map<String, StrategyConfig> strategyDataSource) {
        this.strategyDataSource = strategyDataSource;
    }

    public ModelObjectConvertKey getModelObjectConvertKey() {
        return modelObjectConvertKey;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isShowSQL() {
        return isShowSQL;
    }

    public void setShowSQL(boolean showSQL) {
        isShowSQL = showSQL;
    }

    public boolean isIgnoreEmptySlave() {
        return isIgnoreEmptySlave;
    }

    public void setIgnoreEmptySlave(boolean ignoreEmptySlave) {
        isIgnoreEmptySlave = ignoreEmptySlave;
    }

    public MappingGlobalWrapper getMappingGlobalWrapper() {
        return mappingGlobalWrapper;
    }

    public void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
    }

    public void addMimosaDataSource(MimosaDataSource dataSource) {
        if (StringTools.isEmpty(dataSource.getName())) {
            throw new IllegalArgumentException("数据源必须设置一个名称");
        }
        globalDataSource.put(dataSource.getName(), dataSource);
    }

    public void setMappers(List<String> mappers) {
        this.mappers = mappers;
        if (mappers != null) {
            this.definedLoader = new SQLDefinedLoader(new DefinerConfigure());
            this.definedLoader.load(mappers);
        }
    }

    public void setFactoryBuilderList(List<FactoryBuilder> factoryBuilderList) {
        this.factoryBuilderList = factoryBuilderList;
    }

    public List<? extends IDStrategy> getIdStrategies() {
        return idStrategies;
    }

    public void setIdStrategies(List<? extends IDStrategy> idStrategies) {
        this.idStrategies = idStrategies;
    }

    public List<FactoryBuilder> getAuxFactoryBuilder() {
        if (this.factoryBuilderList != null) {
            return this.factoryBuilderList;
        }
        return null;
    }

    public SQLDefinedLoader getDefinedLoader() {
        return definedLoader;
    }

    public ActionDataSourceWrapper getNewDataSourceWrapper() {
        return new ActionDataSourceWrapper(this);
    }

    public Set<MimosaDataSource> getCurrentDataSources() {
        Set<MimosaDataSource> ds = new LinkedHashSet<MimosaDataSource>();
        MimosaDataSource dataSource = defaultDataSource.getDataSource();
        if (dataSource != null) {
            ds.add(dataSource);
        }

        if (ds.size() > 0) {
            return ds;
        } else {
            return null;
        }
    }

    public MimosaDataSource getDataSourceByName(String dsName) {
        return globalDataSource.get(dsName);
    }
}
