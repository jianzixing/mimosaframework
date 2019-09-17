package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.builder.*;
import org.mimosaframework.orm.convert.ConvertFactory;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.mapping.*;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.sql.SQLException;
import java.util.*;

public class BeanAppContext implements Context {
    private static final Log logger = LogFactory.getLog("init");
    private final ContextValues contextValues;
    private SessionFactoryBuilder sessionFactoryBuilder = null;
    private ConfigBuilder configBuilder = null;

    public BeanAppContext() {
        this.contextValues = new ContextValues();
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
            contextValues.setIdStrategies(applicationInfo.getIdStrategies());

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
            BasicSetting disposition = this.configBuilder.getBasicInfo();
            if (disposition.getConvert() != null) {
                contextValues.setConvert(disposition.getConvert());
            }
            contextValues.setShowSQL(disposition.isShowSQL());
            contextValues.setMappingLevel(disposition.getMappingLevel());
            if (disposition.isIgnoreEmptySlave() != null) {
                contextValues.setIgnoreEmptySlave(disposition.isIgnoreEmptySlave());
            }
        }

        {
            List<MimosaDataSource> dslist = null;
            try {
                dslist = this.configBuilder.getDataSourceList();
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
            contextValues.setChecker(new ModelMeasureChecker(resolvers));
            contextValues.setResolvers(resolvers);
            contextValues.setMappingGlobalWrapper(new MappingGlobalWrapper(this.parseClasses(resolvers)));
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

        {
            ActionDataSourceWrapper wrapper = this.configBuilder.getDefaultDataSourceWrapper().newDataSourceWrapper(contextValues);
            contextValues.setStrategyDataSource(this.configBuilder.getStrategyConfig());
            contextValues.setDefaultDataSource(wrapper);
        }

        this.checkDBMapping();
        ModelObject.addChecker(new ModelMeasureChecker(this.contextValues.getResolvers()));
    }

    private Map<Class, MappingTable> parseClasses(Set<Class> mappingClasses) {
        Map<Class, MappingTable> mappingTables = null;
        if (mappingClasses != null) {
            Map<String, Class> names = new HashMap<>(mappingClasses.size());
            for (Class c : mappingClasses) {
                if (mappingTables == null) {
                    mappingTables = new LinkedHashMap<>();
                }
                DisassembleMappingClass disassembleMappingClass = new DefaultDisassembleMappingClass(c, this.contextValues.getConvert());
                MappingTable mappingTable = disassembleMappingClass.getMappingTable();
                mappingTables.put(c, mappingTable);
                if (names.containsKey(mappingTable.getMappingTableName())) {
                    throw new IllegalArgumentException("已经存在表名称为" + mappingTable.getMappingTableName() + "的映射类,"
                            + names.get(mappingTable.getMappingTableName()) + " 和 " + mappingTable.getMappingClass());
                }
                names.put(mappingTable.getMappingTableName(), c);
            }
        }
        return mappingTables;
    }

    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder() {
        return this.sessionFactoryBuilder;
    }

    private void checkDBMapping() throws ContextException {
        MappingLevel mappingLevel = null;
        if (contextValues != null) {
            mappingLevel = contextValues.getMappingLevel();
        }

        ActionDataSourceWrapper wrapper = contextValues.getDefaultDataSource();

        MappingNamedConvert convert = contextValues.getConvert();
        if (convert == null) convert = ConvertFactory.getDefaultConvert();

        StartCompareMapping compareMapping = CompareMappingFactory.getCompareMapping(mappingLevel, contextValues.getResolvers(), wrapper, convert);
        try {
            compareMapping.doMapping();
            MappingGlobalWrapper databaseWrapper = compareMapping.getWholeMappingDatabase();
            contextValues.setMappingGlobalWrapper(databaseWrapper);
        } catch (SQLException e) {
            throw new ContextException("对比数据库映射出错", e);
        }
    }
}