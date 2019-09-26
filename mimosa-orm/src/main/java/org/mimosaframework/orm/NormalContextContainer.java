package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.convert.ConvertFactory;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.mapping.*;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;
import org.mimosaframework.orm.scripting.DefinerConfigure;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.utils.DatabaseTypeEnum;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class NormalContextContainer implements ContextContainer {
    private static final Log logger = LogFactory.getLog(NormalContextContainer.class);
    private ModelObjectConvertKey modelObjectConvertKey = new SimpleModelObjectConvertKey();
    private List<MimosaDataSource> globalDataSource = new CopyOnWriteArrayList<>();
    private List<FactoryBuilder> factoryBuilderList = new CopyOnWriteArrayList<>();

    private String applicationName;
    private String applicationDetail;
    private MappingLevel mappingLevel;

    private Set<Class> resolvers;
    private Set<MappingTable> mappingTables;
    private MappingGlobalWrapper mappingGlobalWrapper = new MappingGlobalWrapper();
    private MappingNamedConvert convert;

    private ActionDataSourceWrapper defaultDataSource;

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

    @Override
    public MappingTable getMappingTableByClass(Class tableClass) {
        if (this.mappingTables != null) {
            for (MappingTable mappingTable : this.mappingTables) {
                if (mappingTable.getMappingClass().equals(tableClass)) {
                    return mappingTable;
                }
            }
        }
        return null;
    }

    @Override
    public MappingTable getMappingTableByClassName(String tableClassName) {
        if (this.mappingTables != null) {
            for (MappingTable mappingTable : this.mappingTables) {
                if (mappingTable.getMappingClass().getName().equals(tableClassName)) {
                    return mappingTable;
                }
            }
        }
        return null;
    }

    @Override
    public List<MimosaDataSource> getGlobalDataSource() {
        return this.globalDataSource;
    }

    public void setResolvers(Set<Class> resolvers) {
        this.resolvers = resolvers;
    }

    public void setDisassembleResolvers(Set<Class> resolvers) {
        this.resolvers = resolvers;
        if (this.resolvers != null) {
            if (resolvers != null) {
                Map<String, Class> names = new HashMap<>(resolvers.size());
                if (mappingTables == null) {
                    mappingTables = new LinkedHashSet<>();
                }

                for (Class c : resolvers) {
                    DisassembleMappingClass disassembleMappingClass = new DefaultDisassembleMappingClass(c, this.getConvert());
                    MappingTable mappingTable = disassembleMappingClass.getMappingTable();
                    mappingTables.add(mappingTable);
                    if (names.containsKey(mappingTable.getMappingTableName())) {
                        throw new IllegalArgumentException("已经存在表名称为" + mappingTable.getMappingTableName() + "的映射类,"
                                + names.get(mappingTable.getMappingTableName()) + " 和 " + mappingTable.getMappingClass() + "冲突");
                    }
                    names.put(mappingTable.getMappingTableName(), c);
                }

                this.mappingGlobalWrapper.setMappingTables(mappingTables);
            } else {
                logger.warn("您没有配置映射表类信息,当前不会创建任何表");
            }
        } else {
            this.mappingGlobalWrapper.setMappingTables(new LinkedHashSet<MappingTable>());
            logger.warn("没有扫描到表映射类");
        }
    }

    public MappingNamedConvert getConvert() {
        if (convert == null) {
            convert = ConvertFactory.getDefaultConvert();
        }
        return convert;
    }

    public void setConvert(MappingNamedConvert convert) {
        this.convert = convert;
    }

    public ActionDataSourceWrapper getDefaultDataSourceWrapper(boolean isCreateNew) {
        if (defaultDataSource == null) {
            throw new IllegalArgumentException("请设置一个默认数据源");
        }
        if (isCreateNew) {
            return this.defaultDataSource.newDataSourceWrapper();
        } else {
            return defaultDataSource;
        }
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

    public void addMimosaDataSource(MimosaDataSource dataSource) {
        if (StringTools.isEmpty(dataSource.getName())) {
            throw new IllegalArgumentException("数据源必须设置一个名称");
        }
        globalDataSource.add(dataSource);

        if (dataSource.getName().equals(MimosaDataSource.DEFAULT_DS_NAME)) {
            this.defaultDataSource = new ActionDataSourceWrapper(this);
            this.defaultDataSource.setDataSource(dataSource);
        }
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

    @Override
    public MimosaDataSource getDefaultDataSource() {
        for (MimosaDataSource mimosaDataSource : globalDataSource) {
            if (mimosaDataSource.getName() != null
                    && mimosaDataSource.getName().equals(MimosaDataSource.DEFAULT_DS_NAME)) {
                return mimosaDataSource;
            }
        }
        return null;
    }

    @Override
    public MimosaDataSource getAnyDataSource() {
        if (this.getDefaultDataSource() != null) {
            return this.getDefaultDataSource();
        }
        if (this.globalDataSource != null && this.globalDataSource.size() > 0) {
            return this.globalDataSource.get(0);
        }
        return null;
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
        for (MimosaDataSource mimosaDataSource : this.globalDataSource) {
            if (mimosaDataSource.getName() != null && mimosaDataSource.getName().equals(dsName)) {
                return mimosaDataSource;
            }
        }
        return null;
    }

    public Set<MappingTable> getMappingTables() {
        return this.mappingTables;
    }

    /**
     * 开始合并所有的MappingDatabase
     * 从Class获得的MappingTable合并到从数据库获得MappingTable中去
     *
     * @return
     * @throws SQLException
     */
    public void matchWholeMappingDatabase() throws SQLException {
        MimosaDataSource mimosaDataSource = this.getDefaultDataSourceWrapper(false).getDataSource();
        FetchDatabaseMapping fetchDatabaseMapping = new JDBCFetchDatabaseMapping(mimosaDataSource);
        fetchDatabaseMapping.loading();

        MappingDatabase mappingDatabase = fetchDatabaseMapping.getDatabaseMapping();
        // 加载完所有数据库的信息后，将映射类的MappingTable合并到每一个数据库对应的MappingDatabase中去
        Set<MappingTable> mappingTables = this.getMappingTables();

        // 开始合并映射类和数据库表信息
        if (mappingTables != null && mappingDatabase != null) {
            if (mappingDatabase != null) {
                Set<MappingTable> tables = mappingDatabase.getDatabaseTables();
                if (tables != null) {
                    for (MappingTable mpTable : mappingTables) {
                        for (MappingTable dbTable : tables) {
                            if (mpTable.getMappingTableName().equalsIgnoreCase(dbTable.getDatabaseTableName())) {
                                mpTable.applyFromClassMappingTable(dbTable);
                            }
                        }
                    }
                }
            }
        }

        if (globalDataSource != null) {
            Map<MimosaDataSource, MappingDatabase> map = new LinkedHashMap<>();
            map.put(mimosaDataSource, mappingDatabase);
            for (MimosaDataSource ds : this.globalDataSource) {
                if (ds != mimosaDataSource) {
                    fetchDatabaseMapping = new JDBCFetchDatabaseMapping(mimosaDataSource);
                    fetchDatabaseMapping.loading();

                    map.put(ds, fetchDatabaseMapping.getDatabaseMapping());
                }
            }
            this.mappingGlobalWrapper.setDataSourceMappingDatabase(map);
        }

    }

    @Override
    public DatabaseTypeEnum getDatabaseType() {
        MimosaDataSource mimosaDataSource = this.getAnyDataSource();
        if (mimosaDataSource != null) {
            return mimosaDataSource.getDatabaseTypeEnum();
        }
        return null;
    }
}
