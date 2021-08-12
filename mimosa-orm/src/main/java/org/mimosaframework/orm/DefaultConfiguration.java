package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.convert.ConvertFactory;
import org.mimosaframework.orm.convert.NamingConvert;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.*;
import org.mimosaframework.orm.platform.SessionContext;
import org.mimosaframework.orm.scripting.DefinerConfigure;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.transaction.DefaultTransactionFactory;
import org.mimosaframework.orm.transaction.JDBCTransaction;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionFactory;
import org.mimosaframework.orm.utils.DatabaseType;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultConfiguration implements Configuration {
    private static final Log logger = LogFactory.getLog(DefaultConfiguration.class);
    protected ModelObjectConvertKey modelObjectConvertKey = new SimpleModelObjectConvertKey();
    protected Map<String, MimosaDataSource> globalDataSource = new ConcurrentHashMap<>();

    protected String applicationName;
    protected String applicationDetail;
    protected MappingLevel mappingLevel;
    protected TableCompare tableCompare;

    protected Set<Class> resolvers;
    protected Set<MappingTable> mappingTables;
    protected MappingGlobalWrapper mappingGlobalWrapper = new MappingGlobalWrapper();
    protected NamingConvert convert;
    // 数据库表名前缀
    protected String tablePrefix;

    protected SessionContext defaultSessionContext;
    protected MimosaDataSource defaultDataSource;
    protected TransactionFactory transactionFactory;

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

    public TableCompare getTableCompare() {
        return tableCompare;
    }

    public void setTableCompare(TableCompare tableCompare) {
        this.tableCompare = tableCompare;
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
    public Map<String, MimosaDataSource> getGlobalDataSource() {
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

    public SessionContext newSessionContext(String dataSourceName, boolean supportTrans) throws SQLException {
        if (defaultSessionContext == null) {
            throw new IllegalArgumentException(I18n.print("please_set_ds"));
        }
        MimosaDataSource mimosaDataSource = null;
        if (dataSourceName == null || dataSourceName == MimosaDataSource.DEFAULT_DS_NAME) {
            if (this.defaultDataSource == null) {
                throw new NullPointerException(I18n.print("miss_df_datasource"));
            }
            mimosaDataSource = this.defaultDataSource;
        } else {
            mimosaDataSource = this.globalDataSource.get(dataSourceName);
            if (mimosaDataSource == null) {
                throw new NullPointerException(I18n.print("miss_byname_datasource", dataSourceName));
            }
        }
        SessionContext context = this.defaultSessionContext.newSessionContext();
        TransactionFactory transactionFactory = this.getTransactionFactory();
        context.setDataSource(mimosaDataSource);
        DataSource dataSource = mimosaDataSource.getMaster();
        Transaction transaction = null;
        if (supportTrans) {
            transaction = transactionFactory.newTransaction(dataSource);
        } else {
            // 如果不支持事务默认使用JDBCTransaction
            transaction = new JDBCTransaction(dataSource, false);
        }
        context.setTransaction(transaction);
        return context;
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
        if (globalDataSource.size() == 0) {
            // 首先设置第一个为默认连接
            this.defaultDataSource = dataSource;
        }
        if (globalDataSource.get(dataSource.getName()) != null) {
            throw new IllegalStateException(I18n.print("found_ds_same_name", dataSource.getName()));
        }
        globalDataSource.put(dataSource.getName(), dataSource);

        if (dataSource.getName().equals(MimosaDataSource.DEFAULT_DS_NAME)) {
            // 如果有默认连接配置则重置默认连接
            this.defaultDataSource = dataSource;
            this.defaultSessionContext = new SessionContext(this);
            this.defaultSessionContext.setDataSource(dataSource);
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
        return this.defaultDataSource;
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

    public MimosaDataSource getDataSourceByName(String dsName) {
        return this.globalDataSource.get(dsName);
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
            for (Map.Entry<String, MimosaDataSource> entry : this.globalDataSource.entrySet()) {
                try {
                    if (entry != null && entry.getValue() != null) {
                        entry.getValue().close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 不同的事务工厂创建不同的的事务实现
     *
     * @return
     */
    @Override
    public TransactionFactory getTransactionFactory() {
        if (this.transactionFactory == null) {
            synchronized (this) {
                if (this.transactionFactory == null) {
                    this.transactionFactory = new DefaultTransactionFactory();
                }
            }
        }
        return this.transactionFactory;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public Session buildSession() throws SQLException {
        return new DefaultSession(this);
    }

    @Override
    protected DefaultConfiguration clone() {
        DefaultConfiguration configuration = new DefaultConfiguration();
        configuration.globalDataSource = new ConcurrentHashMap<>(globalDataSource);
        configuration.applicationName = applicationName;
        configuration.applicationDetail = applicationDetail;
        configuration.mappingLevel = mappingLevel;
        configuration.tableCompare = tableCompare;
        configuration.resolvers = new LinkedHashSet<>(resolvers);
        configuration.mappingTables = new LinkedHashSet<>(mappingTables);
        configuration.mappingGlobalWrapper = new MappingGlobalWrapper();
        configuration.convert = convert;
        configuration.tablePrefix = tablePrefix;
        configuration.defaultDataSource = defaultDataSource;
        configuration.idStrategies = new ArrayList<>(idStrategies);
        configuration.isShowSQL = isShowSQL;
        configuration.isIgnoreEmptySlave = isIgnoreEmptySlave;
        return configuration;
    }
}
