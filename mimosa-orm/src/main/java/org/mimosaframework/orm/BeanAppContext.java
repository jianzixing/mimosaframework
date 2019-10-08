package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.builder.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.mapping.*;

import java.sql.SQLException;
import java.util.*;

public class BeanAppContext implements Context {
    private static final Log logger = LogFactory.getLog("init");
    private final NormalContextContainer contextValues;
    private SessionFactoryBuilder sessionFactoryBuilder = null;
    private ConfigBuilder configBuilder = null;

    public BeanAppContext() {
        this.contextValues = new NormalContextContainer();
    }

    public BeanAppContext(ConfigBuilder configBuilder) throws ContextException {
        this();
        setBeanAppContext(configBuilder);
    }

    public void setBeanAppContext(ConfigBuilder configBuilder) throws ContextException {
        this.sessionFactoryBuilder = new MimosaSessionFactoryBuilder(contextValues);
        this.configBuilder = configBuilder;
        this.init();
    }

    private void init() throws ContextException {
        contextValues.setContext(this);

        {
            CenterConfigSetting center = this.configBuilder.getCenterInfo();
            if (center != null && center.valid()) {
                // 从配置中心拉取配置
                this.configBuilder = new ConfiguredCenterBuilder();
            }
        }

        {
            ApplicationSetting applicationInfo = this.configBuilder.getApplication();
            contextValues.setApplicationName(applicationInfo.getApplicationName());
            contextValues.setApplicationDetail(applicationInfo.getApplicationDetail());

            List<? extends IDStrategy> strategies = this.configBuilder.getStrategies();
            contextValues.setIdStrategies(strategies);

            System.out.println("" +
                    "------------------------------------------------------------------------------------------------------------\n" +
                    "|     .---.                                                                                                |\n" +
                    "|     |   |                                                                                                |\n" +
                    "|     '---' .--.               _..._                .--.                      .--.    _..._                |\n" +
                    "|     .---. |__|             .'     '.              |__|                      |__|  .'     '.    .--./)    |\n" +
                    "|     |   | .--.            .   .-.   .             .--.                      .--. .   .-.   .  /.''\\\\     |\n" +
                    "|     |   | |  |     __     |  '   '  |             |  |    ____     _____    |  | |  '   '  | | |  | |    |\n" +
                    "|     |   | |  |  .:--.'.   |  |   |  | .--------.  |  |   `.   \\  .'    /    |  | |  |   |  |  \\`-' /     |\n" +
                    "|     |   | |  | / |   \\ |  |  |   |  | |____    |  |  |     `.  `'    .'     |  | |  |   |  |  /(\"'`      |\n" +
                    "|     |   | |  | `\" __ | |  |  |   |  |     /   /   |  |       '.    .'       |  | |  |   |  |  \\ '---.    |\n" +
                    "|     |   | |__|  .'.''| |  |  |   |  |   .'   /    |__|       .'     `.      |__| |  |   |  |   /'\"\"'.\\   |\n" +
                    "|  __.'   '      / /   | |_ |  |   |  |  /    /___           .'  .'`.   `.         |  |   |  |  ||     ||  |\n" +
                    "| |      '       \\ \\._,\\ '/ |  |   |  | |         |        .'   /    `.   `.       |  |   |  |  \\'. __//   |\n" +
                    "| |____.'         `--'  `\"  '--'   '--' |_________|       '----'       '----'      '--'   '--'   `'---'    |\n" +
                    "------------------------------------------------------------------------------------------------------------\n" +
                    "|                                      感谢您使用简子行科技有限公司产品                                          |\n" +
                    "|                                   本公司开源产品完全免费，但请遵守开源协议                                      |\n" +
                    "|                              如您需要定制请访问(http://www.jianzixing.com.cn)                                |\n" +
                    "------------------------------------------------------------------------------------------------------------\n"
            );
        }

        {
            BasicSetting basicInfo = this.configBuilder.getBasicInfo();
            if (basicInfo.getConvert() != null) {
                contextValues.setConvert(basicInfo.getConvert());
            }
            contextValues.setShowSQL(basicInfo.isShowSQL());
            contextValues.setMappingLevel(basicInfo.getMappingLevel());
            if (basicInfo.isIgnoreEmptySlave() != null) {
                contextValues.setIgnoreEmptySlave(basicInfo.isIgnoreEmptySlave());
            }

            contextValues.setInterceptSession(basicInfo.getInterceptSession());
        }

        {
            List<MimosaDataSource> dslist = null;
            try {
                dslist = this.configBuilder.getDataSources();
            } catch (SQLException e) {
                throw new ContextException("获得数据源列表出错", e);
            }
            if (dslist != null) {
                for (MimosaDataSource ds : dslist) {
                    contextValues.addMimosaDataSource(ds);
                }
            }
        }

        {
            Set<Class> resolvers = this.configBuilder.getResolvers();
            contextValues.setDisassembleResolvers(resolvers);
        }

        {
            List<String> mappers = this.configBuilder.getMappers();
            contextValues.setMappers(mappers);
        }

        {
            List<FactoryBuilder> factoryBuilder = null;
            try {
                factoryBuilder = this.configBuilder.getAuxFactoryBuilder();
            } catch (Exception e) {
                throw new ContextException("初始化辅助工具配置类出错", e);
            }
            contextValues.setFactoryBuilderList(factoryBuilder);
        }

        this.checkDBMapping();
        ModelObject.addChecker(new ModelMeasureChecker(contextValues.getMappingTables()));
    }

    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder() {
        return this.sessionFactoryBuilder;
    }

    private void checkDBMapping() throws ContextException {

        try {
            MappingTableWrapper tableWrapper = new MappingTableWrapper(this.contextValues.getMappingTables());

            MimosaDataSource dataSource = this.contextValues.getDefaultDataSourceWrapper(false).getDataSource();
            FetchDatabaseMapping fetchDatabaseMapping = new JDBCFetchDatabaseMapping(dataSource);
            fetchDatabaseMapping.loading();

            MappingDatabase mappingDatabase = fetchDatabaseMapping.getDatabaseMapping();
            if (mappingDatabase != null) {
                NotMatchObject notMatchObject = tableWrapper.getMissingObject(dataSource, mappingDatabase);

                StartCompareMapping compareMapping = CompareMappingFactory.getCompareMapping(
                        contextValues.getMappingLevel(),
                        contextValues.getDefaultDataSourceWrapper(false),
                        notMatchObject
                );
                compareMapping.doMapping();
            }
            contextValues.matchWholeMappingDatabase();
        } catch (SQLException e) {
            throw new ContextException("对比数据库映射出错", e);
        }
    }
}
