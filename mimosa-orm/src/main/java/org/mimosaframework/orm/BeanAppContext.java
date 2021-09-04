package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.builder.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.CompareMappingFactory;
import org.mimosaframework.orm.mapping.CompareMapping;
import org.mimosaframework.orm.mapping.TableCompare;
import org.mimosaframework.orm.transaction.TransactionFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class BeanAppContext implements Context {
    private static final Log logger = LogFactory.getLog("init");
    protected final DefaultConfiguration configuration;
    private SessionFactoryBuilder sessionFactoryBuilder = null;
    private ConfigBuilder configBuilder = null;

    static {
        I18n.register();
    }

    public BeanAppContext() {
        this.configuration = new DefaultConfiguration();
    }

    public BeanAppContext(DefaultConfiguration configuration) {
        this.configuration = configuration;
    }

    public BeanAppContext(ConfigBuilder configBuilder) throws ContextException {
        this();
        setBeanAppContext(configBuilder);
    }

    public void setBeanAppContext(ConfigBuilder configBuilder) throws ContextException {
        this.sessionFactoryBuilder = new MimosaSessionFactoryBuilder(configuration);
        this.configBuilder = configBuilder;
        this.init();
    }

    protected void init() throws ContextException {
        configuration.setContext(this);

        {
            ApplicationSetting applicationInfo = this.configBuilder.getApplication();
            configuration.setApplicationName(applicationInfo.getApplicationName());
            configuration.setApplicationDetail(applicationInfo.getApplicationDetail());

            List<? extends IDStrategy> strategies = this.configBuilder.getStrategies();
            configuration.setIdStrategies(strategies);

            System.out.println("" +
                    "------------------------------------------------------------------------------------------------------------\n" +
                    "|            Yang Ankang provides technical support , Email yak1992@foxmail.com                             |\n" +
                    "------------------------------------------------------------------------------------------------------------\n"
            );
        }

        {
            BasicSetting basicInfo = this.configBuilder.getBasicInfo();
            if (basicInfo.getConvert() != null) {
                configuration.setConvert(basicInfo.getConvert());
            }
            configuration.setShowSQL(basicInfo.isShowSQL());
            configuration.setMappingLevel(basicInfo.getMappingLevel());
            if (basicInfo.getAllowInnerJoin() == false) {
                configuration.setAllowInnerJoin(basicInfo.getAllowInnerJoin());
            }
            if (basicInfo.isIgnoreEmptySlave() != null) {
                configuration.setIgnoreEmptySlave(basicInfo.isIgnoreEmptySlave());
            }
            if (StringTools.isNotEmpty(basicInfo.getTablePrefix())) {
                configuration.setTablePrefix(basicInfo.getTablePrefix());
            }
            if (basicInfo.getUppercase() == true) {
                configuration.setUppercase(basicInfo.getUppercase());
            }
        }

        {
            List<MimosaDataSource> dslist = null;
            try {
                dslist = this.configBuilder.getDataSources();
            } catch (SQLException e) {
                throw new ContextException(I18n.print("get_ds_list_fail"), e);
            }
            TransactionFactory transactionFactory = this.configBuilder.getTransactionFactory();
            if (transactionFactory != null) {
                configuration.setTransactionFactory(transactionFactory);
            }
            if (dslist != null) {
                for (MimosaDataSource ds : dslist) {
                    configuration.addMimosaDataSource(ds);
                }
            }
        }

        {
            Set<Class> resolvers = this.configBuilder.getResolvers();
            configuration.setDisassembleResolvers(resolvers);
        }

        {
            List<String> mappers = this.configBuilder.getMappers();
            configuration.setMappers(mappers);
        }

        {
            TableCompare tableCompare = null;
            try {
                tableCompare = this.configBuilder.getTableCompare();
            } catch (Exception e) {
                throw new ContextException(I18n.print("instance_table_compare_fail"), e);
            }
            configuration.setTableCompare(tableCompare);
        }

        this.initAddition();

        this.checkDBMapping();
        ModelObject.addChecker(new ModelMeasureChecker(configuration.getMappingTables()));
    }

    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder() {
        return this.sessionFactoryBuilder;
    }

    protected void initAddition() {
        // 提供子类额外使用
    }

    protected void checkDBMapping() throws ContextException {
        try {
            if (configuration.getTableCompare() != null) {
                CompareMapping compareMapping = CompareMappingFactory.getCompareMapping(
                        MappingLevel.UPDATE,
                        configuration.mappingGlobalWrapper,
                        configuration.newSessionContext(MimosaDataSource.DEFAULT_DS_NAME, false)
                );
                configuration.getTableCompare().doMapping(configuration.clone(), compareMapping);
            } else {
                CompareMapping compareMapping = CompareMappingFactory.getCompareMapping(
                        configuration.getMappingLevel(),
                        configuration.mappingGlobalWrapper,
                        configuration.newSessionContext(MimosaDataSource.DEFAULT_DS_NAME, false)
                );
                compareMapping.doMapping();
            }
        } catch (SQLException | ContextException e) {
            if (e instanceof ContextException) throw (ContextException) e;
            throw new ContextException(I18n.print("compare_db_error"), e);
        }
    }
}
