package org.mimosaframework.orm.spring;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.builder.*;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.platform.SimpleTemplate;
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
import java.util.Set;

/**
 * spring bean 用来初始化整个程序
 *
 * @author yangankang
 */
public class SpringMimosaSessionFactory extends AbstractConfigBuilder implements InitializingBean, ApplicationContextAware, SpringContextHolder, SessionFactory {
    protected BeanAppContext context;

    protected ApplicationContext applicationContext;
    protected SessionFactoryBuilder sessionFactoryBuilder;
    protected SessionFactory sessionFactory = null;
    protected AbstractInterceptSession interceptSession;

    protected ApplicationSetting applicationSetting = new ApplicationSetting();
    protected BasicSetting basicSetting = new BasicSetting();
    protected CenterConfigSetting centerConfigSetting = null;
    protected MimosaDataSource defaultDataSource = null;
    protected Set<Class> resolvers = null;
    protected List<MimosaDataSource> dataSources = new ArrayList<>();

    protected String applicationName;
    protected String applicationDetail;
    protected boolean isShowSQL;
    protected MappingNamedConvert convert;
    protected String convertType;
    protected String scanPackage;
    protected Set<String> mappingClasses;
    protected DataSource dataSource;
    protected MappingLevel mappingLevel;
    protected Boolean ignoreEmptySlave;

    protected String mapper;
    protected List<String> mappers;
    protected List<FactoryBuilder> auxFactoryBuilder;
    protected List<? extends IDStrategy> strategies;

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

    public void setConvert(MappingNamedConvert convert) {
        this.convert = convert;
        this.basicSetting.setConvert(convert);
    }

    public void setConvertType(String convertType) throws ContextException {
        this.convertType = convertType;
        this.basicSetting.setConvert(super.getConvert(null, convertType, null));
    }

    public void setCenterConfigSetting(CenterConfigSetting centerConfigSetting) throws ContextException {
        this.centerConfigSetting = centerConfigSetting;
        if (!this.centerConfigSetting.valid()) {
            throw new ContextException(Messages.get(LanguageMessageFactory.PROJECT,
                    SpringMimosaSessionFactory.class, "center_config_fail"));
        }
    }

    public void setInterceptSession(AbstractInterceptSession interceptSession) {
        this.interceptSession = interceptSession;
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
    }


    public void setMappingLevel(MappingLevel mappingLevel) {
        this.mappingLevel = mappingLevel;
        this.basicSetting.setMappingLevel(mappingLevel);
    }

    public void setDefaultDataSource(MimosaDataSource defaultDataSource) throws SQLException {
        if (defaultDataSource != null) {
            this.defaultDataSource = defaultDataSource;
            this.defaultDataSource.setName(MimosaDataSource.DEFAULT_DS_NAME);
            this.dataSources.add(this.defaultDataSource);
        }
    }

    public void setDataSource(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        if (this.defaultDataSource == null) {
            this.defaultDataSource = new MimosaDataSource(dataSource, MimosaDataSource.DEFAULT_DS_NAME);
            this.dataSources.add(this.defaultDataSource);
        }
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public void setMappingClasses(Set<String> mappingClasses) {
        this.mappingClasses = mappingClasses;
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

    public void setDataSources(List<MimosaDataSource> dataSources) {
        if (dataSources != null) {
            this.dataSources.addAll(dataSources);
        }
        if (this.dataSources == null || this.dataSources.size() == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    SpringMimosaSessionFactory.class, "ds_miss"));
        }
        boolean is = false;
        for (MimosaDataSource mimosaDataSource : this.dataSources) {
            if (MimosaDataSource.DEFAULT_DS_NAME.equals(mimosaDataSource.getName())) {
                is = true;
            }
        }
        if (!is) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    SpringMimosaSessionFactory.class, "ds_default_miss"));
        }
    }

    @Override
    public List<MimosaDataSource> getDataSources() throws SQLException {
        if (dataSources != null) {
            List<MimosaDataSource> dslist = new ArrayList<>();
            for (MimosaDataSource ds : dataSources) {
                dslist.add(ds);
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
    public List<? extends IDStrategy> getStrategies() {
        return this.strategies;
    }

    @Override
    public BasicSetting getBasicInfo() throws ContextException {
        this.basicSetting.setInterceptSession(interceptSession);
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
