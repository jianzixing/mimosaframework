package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.utils.DatabaseType;
import org.mimosaframework.orm.utils.SQLUtils;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MimosaDataSource implements Closeable {
    private static final Log logger = LogFactory.getLog(MimosaDataSource.class);
    private static final Map<DataSource, DatabaseType> dataSourceInfo = new ConcurrentHashMap<>();
    private String name;
    private DataSource master;
    private Map<String, DataSource> slaves;
    private DatabaseType databaseTypeEnum;
    private String destroyMethod; // 连接池关闭方法

    public static final String DEFAULT_DS_NAME = "default";


    public static int getDataSourceSize() {
        return dataSourceInfo.size();
    }

    public static Set<DataSource> getAllDataSources() {
        Set<Map.Entry<DataSource, DatabaseType>> entries = dataSourceInfo.entrySet();
        Set<DataSource> dataSources = new LinkedHashSet<>();
        for (Map.Entry<DataSource, DatabaseType> entry : entries) {
            dataSources.add(entry.getKey());
        }
        return dataSources;
    }

    public MimosaDataSource() {
    }

    public static void clearAllDataSources() {
        if (dataSourceInfo != null) {
            Iterator<Map.Entry<DataSource, DatabaseType>> iterator = dataSourceInfo.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<DataSource, DatabaseType> entry = iterator.next();
                DataSource dataSource = entry.getKey();
                if (dataSource instanceof Closeable) {
                    try {
                        ((Closeable) dataSource).close();
                    } catch (IOException e) {
                        logger.error(I18n.print("close_ds_error"), e);
                    }
                }
            }
            dataSourceInfo.clear();
        }
    }

    private void loadDatabaseType() throws SQLException {
        if (master != null) {
            DatabaseType dte = dataSourceInfo.get(master);
            if (dte == null) {
                this.databaseTypeEnum = SQLUtils.getDatabaseType(master);
                dataSourceInfo.put(master, databaseTypeEnum);
            } else {
                this.databaseTypeEnum = dte;
            }
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
            throw new IllegalArgumentException(I18n.print("not_found_master"));
        }
        return master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaster(DataSource master) throws SQLException {
        this.master = master;
        this.loadDatabaseType();
    }

    public void setSlaves(Map<String, DataSource> slaves) {
        this.slaves = slaves;
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
                    logger.warn(I18n.print("not_found_slave"));
                }
                return master;
            } else {
                throw new IllegalArgumentException(I18n.print("not_found_slave_please"));
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
                            throw new IllegalArgumentException(I18n.print("not_found_slave_config", name, name));
                        } else {
                            return this.getMaster();
                        }
                    }
                } else {
                    return this.randomSlave(isIgnoreEmptySlave);
                }
            } else {
                if (!isIgnoreEmptySlave) {
                    throw new IllegalArgumentException(I18n.print("not_found_slave_config", name, name));
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
            return MimosaConnection.getConnection(ds);
        } else {
            ds = this.getSalveDataSource(slaveName, isIgnoreEmptySlave);
            return MimosaConnection.getConnection(ds);
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

    public DatabaseType getDatabaseTypeEnum() {
        return databaseTypeEnum;
    }

    public boolean hasMaster() {
        return this.master == null ? false : true;
    }

    public String getDestroyMethod() {
        return destroyMethod;
    }

    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    @Override
    public Object clone() {
        MimosaDataSource ds = new MimosaDataSource();
        ds.master = master;
        ds.slaves = slaves;
        ds.databaseTypeEnum = databaseTypeEnum;
        return ds;
    }

    public void close() throws IOException {
        if (this.master != null) {
            if (this.master instanceof Closeable) {
                ((Closeable) this.master).close();
            } else if (StringTools.isNotEmpty(this.destroyMethod)) {
                Method[] methods = this.master.getClass().getMethods();
                for (Method m : methods) {
                    if (m.getName().equals(this.destroyMethod)) {
                        try {
                            m.invoke(this.master);
                        } catch (Exception e) {
                            throw new IOException(I18n.print("run_close_db_error"), e);
                        }
                    }
                }
            }
        }

        if (this.slaves != null) {
            Set<Map.Entry<String, DataSource>> set = this.slaves.entrySet();
            for (Map.Entry<String, DataSource> entry : set) {
                DataSource ds = entry.getValue();
                if (ds != null) {
                    if (ds instanceof Closeable) {
                        ((Closeable) ds).close();
                    } else if (StringTools.isNotEmpty(this.destroyMethod)) {
                        Method[] methods = ds.getClass().getMethods();
                        for (Method m : methods) {
                            if (m.getName().equals(this.destroyMethod)) {
                                try {
                                    m.invoke(ds);
                                } catch (Exception e) {
                                    throw new IOException(I18n.print("run_close_db_error"), e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
