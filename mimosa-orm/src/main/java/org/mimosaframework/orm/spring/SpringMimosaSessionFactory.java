package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.*;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.builder.*;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;
import org.mimosaframework.orm.platform.SimpleTemplate;
import org.mimosaframework.orm.strategy.StrategyConfig;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionPropagationType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * spring bean 用来初始化整个程序
 *
 * @author yangankang
 */
public class SpringMimosaSessionFactory extends AbstractConfigBuilder implements InitializingBean, ApplicationContextAware, SpringContextHolder, SessionFactory {
    private BeanAppContext context;

    private ApplicationContext applicationContext;
    private SessionFactoryBuilder sessionFactoryBuilder;
    private SessionFactory sessionFactory = null;

    private ApplicationSetting applicationSetting = new ApplicationSetting();
    private BasicSetting basicSetting = new BasicSetting();
    private CenterConfigSetting centerConfigSetting = null;
    private MimosaDataSource defaultDataSource = null;
    private Set<Class> resolvers = null;
    private Map<String, StrategyConfig> strategyConfig = null;
    private ActionDataSourceWrapper defaultDataSourceWrapper = null;
    private List<SpringDataSource> dataSourceWrapper = null;

    private String applicationName;
    private String applicationDetail;
    private boolean isShowSQL;
    private MappingNamedConvert convert;
    private String convertType;
    private boolean isCluster;
    private String scanPackage;
    private Set<String> mappingClasses;
    private DataSource dataSource;
    private MappingLevel mappingLevel;
    private Boolean ignoreEmptySlave;

    private String mapper;
    private List<String> mappers;
    private List<FactoryBuilder> auxFactoryBuilder;
    private List<? extends IDStrategy> strategies;
    private String info;

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        this.applicationSetting.setApplicationName(applicationName);
    }

    public void setApplicationDetail(String applicationDetail) {
        this.applicationDetail = applicationDetail;
        this.applicationSetting.setApplicationDetail(applicationDetail);
    }

    public void setShowSQL(boolean showSQL) {
        isShowSQL = showSQL;
        this.basicSetting.setShowSQL(isShowSQL);
    }

    public void setIgnoreEmptySlave(Boolean ignoreEmptySlave) {
        this.ignoreEmptySlave = ignoreEmptySlave;
        this.basicSetting.setIgnoreEmptySlave(ignoreEmptySlave);
    }

    public void setInfo(String info) {
        this.info = info;
        this.applicationSetting.setInfo(info);
    }

    public void setConvert(MappingNamedConvert convert) {
        this.convert = convert;
        this.basicSetting.setConvert(convert);
    }

    public void setConvertType(String convertType) throws ContextException {
        this.convertType = convertType;
        this.basicSetting.setConvert(super.getConvert(null, convertType));
    }

    public void setCenterConfigSetting(CenterConfigSetting centerConfigSetting) throws ContextException {
        this.centerConfigSetting = centerConfigSetting;
        if (!this.centerConfigSetting.valid()) {
            throw new ContextException("配置中心配置不完整");
        }
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public void setMappers(List<String> mappers) {
        this.mappers = mappers;
    }

    public void setAuxFactoryBuilder(List<FactoryBuilder> auxFactoryBuilder) {
        this.auxFactoryBuilder = auxFactoryBuilder;
    }

    public void setStrategies(List<? extends IDStrategy> strategies) {
        this.strategies = strategies;
        this.applicationSetting.setIdStrategies(strategies);
    }


    public void setMappingLevel(MappingLevel mappingLevel) {
        this.mappingLevel = mappingLevel;
        this.basicSetting.setMappingLevel(mappingLevel);
    }

    public void setDefaultDataSource(SpringDataSource defaultDataSource) throws SQLException {
        if (defaultDataSource != null) {
            this.defaultDataSource = defaultDataSource.toMimosaDataSource("default");
        }
    }

    public void setDataSource(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        if (this.defaultDataSource == null) {
            this.defaultDataSource = new MimosaDataSource(dataSource, "default");
        }
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public void setMappingClasses(Set<String> mappingClasses) {
        this.mappingClasses = mappingClasses;
    }

    public void setStrategyConfig(Map<String, StrategyConfig> strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

    public SpringMimosaSessionFactory() {
        this.context = new BeanAppContext();
    }

    /**
     * 初始化整个框架，包括表映射、创建等等
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        this.resolvers = this.getMappingClass();
        this.defaultDataSourceWrapper = new ActionDataSourceWrapper();
        this.defaultDataSourceWrapper.setDataSource(defaultDataSource);

        context.setBeanAppContext(this);
        this.sessionFactoryBuilder = context.getSessionFactoryBuilder();
        this.sessionFactory = this.sessionFactoryBuilder.build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Session openSession() throws MimosaException {
        return this.sessionFactory.openSession();
    }

    @Override
    public Session getCurrentSession() throws MimosaException {
        return this.sessionFactory.getCurrentSession();
    }


    @Override
    public Transaction beginTransaction() throws TransactionException {
        return this.sessionFactory.beginTransaction();
    }

    @Override
    public Transaction beginTransaction(TransactionPropagationType pt) throws TransactionException {
        return this.sessionFactory.beginTransaction(pt);
    }

    @Override
    public Transaction beginTransaction(TransactionIsolationType it) throws TransactionException {
        return this.sessionFactory.beginTransaction(it);
    }

    @Override
    public Transaction beginTransaction(TransactionPropagationType pt, TransactionIsolationType it) throws TransactionException {
        return this.sessionFactory.beginTransaction(pt, it);
    }

    @Override
    public Transaction createTransaction() {
        return this.sessionFactory.createTransaction();
    }

    @Override
    public Transaction createTransaction(TransactionPropagationType pt) {
        return this.sessionFactory.createTransaction(pt);
    }

    @Override
    public Transaction createTransaction(TransactionIsolationType it) {
        return this.sessionFactory.createTransaction(it);
    }

    @Override
    public Transaction createTransaction(TransactionPropagationType pt, TransactionIsolationType it) {
        return this.sessionFactory.createTransaction(pt, it);
    }

    @Override
    public SimpleTemplate getSimpleTemplate(String dsname) {
        return this.sessionFactory.getSimpleTemplate(dsname);
    }

    @Override
    public ApplicationContext getSpringContext() {
        return this.applicationContext;
    }

    @Override
    public Object getBean(String beanName) {
        return this.applicationContext.getBean(beanName);
    }

    @Override
    public String getBeanName(Object object) {
        String[] names = this.applicationContext.getBeanDefinitionNames();
        if (names != null) {
            for (String name : names) {
                if (this.applicationContext.getBean(name).equals(object)) {
                    return name;
                }
            }
        }
        return null;
    }

    @Override
    protected String getMappingClassPackage() {
        return this.scanPackage;
    }

    @Override
    protected Set<String> getAdditionMappingClass() {
        return this.mappingClasses;
    }

    @Override
    public ApplicationSetting getApplication() {
        return this.applicationSetting;
    }

    @Override
    public CenterConfigSetting getCenterInfo() throws ContextException {
        return this.centerConfigSetting;
    }
    
    @Override
    public MimosaDataSource getDefaultDataSource() {
        return this.defaultDataSource;
    }

    public void setDataSourceWrapper(List<SpringDataSource> dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }

    @Override
    public List<MimosaDataSource> getDataSourceList() throws SQLException {
        if (dataSourceWrapper != null) {
            List<MimosaDataSource> dslist = new ArrayList<>();
            for (SpringDataSource ds : dataSourceWrapper) {
                dslist.add(ds.toMimosaDataSource());
            }
            return dslist;
        }
        return null;
    }

    @Override
    public Set<Class> getResolvers() throws ContextException {
        return this.resolvers;
    }

    @Override
    public Map<String, StrategyConfig> getStrategyConfig() {
        return this.strategyConfig;
    }

    @Override
    public ActionDataSourceWrapper getDefaultDataSourceWrapper() {
        return this.defaultDataSourceWrapper;
    }

    @Override
    public BasicSetting getBasicInfo() throws ContextException {
        return this.basicSetting;
    }

    @Override
    public List<String> getMappers() {
        List<String> mps = new ArrayList<>();
        if (this.mappers != null) mps.addAll(this.mappers);
        if (this.mapper != null) mps.add(this.mapper);
        return mps;
    }

    @Override
    public List<FactoryBuilder> getAuxFactoryBuilder() {
        return this.auxFactoryBuilder;
    }
}
