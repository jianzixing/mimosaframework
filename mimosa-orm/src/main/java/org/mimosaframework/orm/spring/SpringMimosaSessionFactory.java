package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.*;
import org.mimosaframework.orm.builder.AbstractConfigBuilder;
import org.mimosaframework.orm.builder.ApplicationSetting;
import org.mimosaframework.orm.builder.BasicSetting;
import org.mimosaframework.orm.builder.CenterConfigSetting;
import org.mimosaframework.orm.convert.NamingConvert;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.TableCompare;
import org.mimosaframework.orm.transaction.TransactionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
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

    protected ApplicationSetting applicationSetting = new ApplicationSetting();
    protected BasicSetting basicSetting = new BasicSetting();
    protected CenterConfigSetting centerConfigSetting = null;
    protected MimosaDataSource defaultDataSourceBundle = null;
    protected Set<Class> resolvers = null;
    protected List<MimosaDataSource> dataSources = new ArrayList<>();

    protected String applicationName;
    protected String applicationDetail;
    protected boolean isShowSQL;
    protected NamingConvert convert;
    protected String convertType;
    protected String scanPackage;
    protected String tablePrefix;
    protected Set<String> mappingClasses;
    protected DataSource dataSource;
    protected MappingLevel mappingLevel;
    protected TableCompare tableCompare;
    protected Boolean ignoreEmptySlave;

    protected String mapper;
    protected List<String> mappers;
    protected List<? extends IDStrategy> strategies;

    /**
     * 获取spring的事务管理器
     */
    private PlatformTransactionManager transactionManager;

    @Autowired(required = false)
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

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

    public void setConvert(NamingConvert convert) {
        this.convert = convert;
        this.basicSetting.setConvert(convert);
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        this.basicSetting.setTablePrefix(tablePrefix);
    }

    public void setConvertType(String convertType) throws ContextException {
        this.convertType = convertType;
        this.basicSetting.setConvert(super.getConvert(null, convertType, null));
    }

    public void setCenterConfigSetting(CenterConfigSetting centerConfigSetting) throws ContextException {
        this.centerConfigSetting = centerConfigSetting;
        if (!this.centerConfigSetting.valid()) {
            throw new ContextException(I18n.print("center_config_fail"));
        }
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public void setMappers(List<String> mappers) {
        this.mappers = mappers;
    }

    public void setStrategies(List<? extends IDStrategy> strategies) {
        this.strategies = strategies;
    }


    public void setMappingLevel(MappingLevel mappingLevel) {
        this.mappingLevel = mappingLevel;
        this.basicSetting.setMappingLevel(mappingLevel);
    }

    public void setTableCompare(TableCompare tableCompare) {
        this.tableCompare = tableCompare;
    }

    public void setDefaultDataSourceBundle(MimosaDataSource defaultDataSource) throws SQLException {
        if (defaultDataSource != null) {
            this.defaultDataSourceBundle = defaultDataSource;
            this.defaultDataSourceBundle.setName(MimosaDataSource.DEFAULT_DS_NAME);
            this.dataSources.add(this.defaultDataSourceBundle);
        }
    }

    public void addDataSourceBundle(MimosaDataSource defaultDataSource) {

    }

    public void setDataSource(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        if (this.defaultDataSourceBundle == null) {
            this.defaultDataSourceBundle = new MimosaDataSource(dataSource, MimosaDataSource.DEFAULT_DS_NAME);
            this.dataSources.add(this.defaultDataSourceBundle);
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
    public Configuration getConfiguration() {
        return this.sessionFactory.getConfiguration();
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

    public void setDataSources(List<MimosaDataSource> dataSources) {
        if (dataSources != null) {
            this.dataSources.addAll(dataSources);
        }
        if (this.dataSources == null || this.dataSources.size() == 0) {
            throw new IllegalArgumentException(I18n.print("ds_miss"));
        }
        boolean is = false;
        for (MimosaDataSource mimosaDataSource : this.dataSources) {
            if (MimosaDataSource.DEFAULT_DS_NAME.equals(mimosaDataSource.getName())) {
                is = true;
            }
        }
        if (!is) {
            throw new IllegalArgumentException(I18n.print("ds_default_miss"));
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
    public TransactionFactory getTransactionFactory() {
        return new SpringTransactionFactory(transactionManager);
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
    public TableCompare getTableCompare() {
        return this.tableCompare;
    }
}
