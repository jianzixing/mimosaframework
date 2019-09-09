package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.utils.DataSourceUtils;
import org.mimosaframework.orm.utils.DatabaseTypeEnum;
import org.mimosaframework.orm.utils.SQLUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class MimosaDataSource {
    public static final String DEFAULT_DS_NAME = "default";
    private static final Log logger = LogFactory.getLog(MimosaDataSource.class);
    private static final Map<DataSource, DatabaseTypeEnum> dataSourceInfo = new ConcurrentHashMap<>();
    private static final Set<DataSource> dataSources = new CopyOnWriteArraySet<>();
    private String name;
    private DataSource master;
    private Map<String, DataSource> slaves;
    private DatabaseTypeEnum databaseTypeEnum;


    public static int getDataSourceSize() {
        return dataSourceInfo.size();
    }

    public static Set<DataSource> getAllDataSources() {
        // return Collections.unmodifiableSet(dataSources);
        return dataSources;
    }

    public MimosaDataSource() {
    }

    private void loadDatabaseType() throws SQLException {
        DatabaseTypeEnum dte = dataSourceInfo.get(master);
        if (dte == null) {
            this.databaseTypeEnum = SQLUtils.getDatabaseType(master);
            dataSourceInfo.put(master, databaseTypeEnum);
            dataSources.add(master);
            if (slaves != null) {
                Iterator<Map.Entry<String, DataSource>> sls = slaves.entrySet().iterator();
                while (sls.hasNext()) {
                    dataSources.add(sls.next().getValue());
                }
            }
        } else {
            this.databaseTypeEnum = dte;
        }
    }

    public MimosaDataSource(DataSource master, String name) throws SQLException {
        this();
        this.name = name;
        this.master = master;
        this.loadDatabaseType();
    }

    public MimosaDataSource(DataSource master, Map<String, DataSource> slaves, String name) throws SQLException {
        this.name = name;
        this.master = master;
        this.slaves = slaves;
        this.loadDatabaseType();
    }

    public DataSource getMaster() {
        if (master == null) {
            throw new IllegalArgumentException("没有找到 master 数据库 DataSource");
        }
        return master;
    }

    public String getName() {
        return name;
    }

    public Map<String, DataSource> getSlaves() {
        if (slaves != null) {
            return new LinkedHashMap<>(slaves);
        }
        return null;
    }

    public DataSource randomSlave(boolean isIgnoreEmptySlave) {
        if (slaves != null && slaves.size() > 0) {
            int max = slaves.size() - 1;
            int min = 0;
            int s = (int) Math.round(Math.random() * (max - min) + min);
            return slaves.get(s);
        } else {
            if (isIgnoreEmptySlave) {
                if (logger.isWarnEnabled()) {
                    logger.warn("没有找到从数据库配置,切换到主库链接!");
                }
                return master;
            } else {
                throw new IllegalArgumentException("没有找到从数据库配置,请先配置从数据库!");
            }
        }
    }

    public DataSource getDataSource(boolean isMaster) {
        return this.getDataSource(isMaster, null, false);
    }

    public DataSource getSalveDataSource(String name, boolean isIgnoreEmptySlave) {
        return this.getDataSource(false, name, isIgnoreEmptySlave);
    }

    private DataSource getDataSource(boolean isMaster, String name, boolean isIgnoreEmptySlave) {
        if (isMaster) {
            return this.getMaster();
        } else {
            if (this.slaves != null) {
                if (StringTools.isNotEmpty(name)) {
                    DataSource dataSource = this.slaves.get(name);
                    if (isIgnoreEmptySlave && dataSource == null && slaves != null && slaves.size() > 0) {
                        return this.randomSlave(true);
                    } else {
                        if (!isIgnoreEmptySlave) {
                            throw new IllegalArgumentException("没有找到从数据库 " + name + " ,请先配置名称为 " + name + " 的从数据库!");
                        } else {
                            return this.getMaster();
                        }
                    }
                } else {
                    return this.randomSlave(isIgnoreEmptySlave);
                }
            } else {
                if (!isIgnoreEmptySlave) {
                    throw new IllegalArgumentException("没有找到从数据库 " + name + " ,请先配置名称为 " + name + " 的从数据库!");
                } else {
                    return this.getMaster();
                }
            }
        }
    }

    /**
     * 获得一个新的连接
     *
     * @param isMaster
     * @param slaveName
     * @return
     * @throws SQLException
     */
    public Connection getConnection(boolean isMaster, String slaveName, boolean isIgnoreEmptySlave) throws SQLException {
        DataSource ds = null;
        if (isMaster) {
            ds = this.getDataSource(true);
            return DataSourceUtils.getConnection(ds);
        } else {
            ds = this.getSalveDataSource(slaveName, isIgnoreEmptySlave);
            return DataSourceUtils.getConnection(ds);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MimosaDataSource) {
            MimosaDataSource msd = (MimosaDataSource) obj;
            if (this.getMaster().equals(msd.getMaster())) {
                Map<String, DataSource> dsList = this.getSlaves();
                if (this.getSlaves() == null && msd.getSlaves() == null) {
                    return true;
                }
                if (this.getSlaves() == null && msd.getSlaves() != null && msd.getSlaves().size() != 0) {
                    return false;
                }
                if (msd.getSlaves() == null && this.getSlaves() != null && this.getSlaves().size() != 0) {
                    return false;
                }
                Iterator<Map.Entry<String, DataSource>> iterator = dsList.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, DataSource> entry = iterator.next();
                    if (!msd.getSlaves().containsValue(entry.getValue())) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    public DatabaseTypeEnum getDatabaseTypeEnum() {
        return databaseTypeEnum;
    }

    public boolean hasMaster() {
        return this.master == null ? false : true;
    }

    @Override
    public Object clone() {
        MimosaDataSource ds = new MimosaDataSource();
        ds.master = master;
        ds.slaves = slaves;
        ds.databaseTypeEnum = databaseTypeEnum;
        return ds;
    }
}
