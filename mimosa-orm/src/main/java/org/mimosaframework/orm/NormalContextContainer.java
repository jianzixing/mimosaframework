package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.convert.ConvertFactory;
import org.mimosaframework.orm.convert.NamingConvert;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.DefaultDisassembleMappingClass;
import org.mimosaframework.orm.mapping.DisassembleMappingClass;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.DataSourceWrapper;
import org.mimosaframework.orm.scripting.DefinerConfigure;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.transaction.TransactionFactory;
import org.mimosaframework.orm.utils.DatabaseType;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class NormalContextContainer implements ContextContainer {
    private static final Log logger = LogFactory.getLog(NormalContextContainer.class);
    protected ModelObjectConvertKey modelObjectConvertKey = new SimpleModelObjectConvertKey();
    protected List<MimosaDataSource> globalDataSource = new CopyOnWriteArrayList<>();

    /**
     * 可以通过配置文件制定自定义的session实现,比如要
     * 实现一个分库分表的session，或者分布式的实现等等
     */
    protected AbstractInterceptSession interceptSession;

    protected String applicationName;
    protected String applicationDetail;
    protected MappingLevel mappingLevel;

    protected Set<Class> resolvers;
    protected Set<MappingTable> mappingTables;
    protected MappingGlobalWrapper mappingGlobalWrapper = new MappingGlobalWrapper();
    protected NamingConvert convert;
    // 数据库表名前缀
    protected String tablePrefix;

    protected DataSourceWrapper defaultDataSource;

    protected List<? extends IDStrategy> idStrategies;
    protected boolean isShowSQL = false;
    protected List<String> mappers;
    protected SQLDefinedLoader definedLoader;

    /**
     * True : 在选择从库时如果没有从库则选择主库,防止报错
     * False : 在选择从库时如果没有从库返回为空直接报错
     */
    protected boolean isIgnoreEmptySlave = true;

    protected Context context;

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

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
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
                    DisassembleMappingClass disassembleMappingClass = new
                            DefaultDisassembleMappingClass(c, this.getConvert(), this.tablePrefix);
                    MappingTable mappingTable = disassembleMappingClass.getMappingTable();
                    mappingTables.add(mappingTable);
                    if (names.containsKey(mappingTable.getMappingTableName())) {
                        throw new IllegalArgumentException(I18n.print("conflict_name",
                                mappingTable.getMappingTableName(), names.get(mappingTable.getMappingTableName()).getName(),
                                mappingTable.getMappingClass().getName()));
                    }
                    names.put(mappingTable.getMappingTableName(), c);
                }

                this.mappingGlobalWrapper.setMappingTables(mappingTables);
            } else {
                logger.warn(I18n.print("empty_mapping_class"));
            }
        } else {
            this.mappingGlobalWrapper.setMappingTables(new LinkedHashSet<MappingTable>());
            logger.warn(I18n.print("not_scan_class"));
        }
    }

    public NamingConvert getConvert() {
        if (convert == null) {
            convert = ConvertFactory.getDefaultConvert();
        }
        return convert;
    }

    public void setConvert(NamingConvert convert) {
        this.convert = convert;
    }

    public DataSourceWrapper getDefaultDataSourceWrapper(boolean isCreateNew) {
        if (defaultDataSource == null) {
            throw new IllegalArgumentException(I18n.print("please_set_ds"));
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
            throw new IllegalArgumentException(I18n.print("must_ds_name"));
        }
        globalDataSource.add(dataSource);

        if (dataSource.getName().equals(MimosaDataSource.DEFAULT_DS_NAME)) {
            this.defaultDataSource = new DataSourceWrapper(this);
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

    public List<? extends IDStrategy> getIdStrategies() {
        return idStrategies;
    }

    public void setIdStrategies(List<? extends IDStrategy> idStrategies) {
        this.idStrategies = idStrategies;
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

    public DataSourceWrapper getNewDataSourceWrapper() {
        return new DataSourceWrapper(this);
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

    public void setMappingTables(Set<MappingTable> mappingTables) {
        this.mappingTables = mappingTables;
    }

    @Override
    public DatabaseType getDatabaseType() {
        MimosaDataSource mimosaDataSource = this.getAnyDataSource();
        if (mimosaDataSource != null) {
            return mimosaDataSource.getDatabaseTypeEnum();
        }
        return null;
    }

    @Override
    public void clearMimosaDataSources() {
        if (this.globalDataSource != null) {
            for (MimosaDataSource mimosaDataSource : this.globalDataSource) {
                try {
                    mimosaDataSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public AbstractInterceptSession getInterceptSession() {
        return this.interceptSession;
    }

    /**
     * 不同的事务工厂创建不同的的事务实现
     *
     * @return
     */
    @Override
    public TransactionFactory getTransactionFactory() {
        return null;
    }

    @Override
    public Session buildSession() {
        return new DefaultSession(this);
    }

    public void setInterceptSession(AbstractInterceptSession interceptSession) {
        this.interceptSession = interceptSession;
    }
}
