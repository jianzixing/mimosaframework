package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.ContextContainer;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.JDBCTransaction;
import org.mimosaframework.orm.utils.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceWrapper {
    private ContextContainer contextValues;
    private MimosaDataSource dataSource;
    private boolean isMaster = true;
    private String slaveName;
    private Connection connection;

    /**
     * 如果为true则不受事务影响使用后即关闭(调用close方法)
     */
    private boolean isAutoCloseConnection = false;

    public DataSourceWrapper() {
    }

    public DataSourceWrapper(ContextContainer contextValues) {
        this.contextValues = contextValues;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            if (isAutoCloseConnection) {
                connection = this.dataSource.getConnection(isMaster, slaveName, contextValues.isIgnoreEmptySlave());
            } else {
                Transaction transaction = JDBCTransaction.getLastTransaction(contextValues);
                if (transaction == null) {
                    connection = this.dataSource.getConnection(isMaster, slaveName, contextValues.isIgnoreEmptySlave());
                } else {
                    connection = transaction.getConnection(dataSource);
                    return connection;
                }
            }
        }
        return connection;
    }

    public void close() throws SQLException {
        if (this.connection != null) {
            Transaction transaction = JDBCTransaction.getLastTransaction(contextValues);
            if (transaction != null && transaction.getConnection(dataSource) == this.connection) {
                return;
            }
            this.connection.close();
            this.connection = null;
        }
    }

    public String getSlaveName() {
        return slaveName;
    }

    public void setSlaveName(String slaveName) {
        this.slaveName = slaveName;
    }

    public JDBCExecutor getDBChanger() {
        return new DefaultJDBCExecutor(this);
    }

    public DatabaseType getDatabaseTypeEnum() {
        return this.dataSource.getDatabaseTypeEnum();
    }

    public MimosaDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MimosaDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public boolean isShowSql() {
        return contextValues.isShowSQL();
    }

    public boolean isAutoCloseConnection() {
        return isAutoCloseConnection;
    }

    public void setAutoCloseConnection(boolean autoCloseConnection) {
        isAutoCloseConnection = autoCloseConnection;
    }

    public boolean isIgnoreEmptySlave() {
        return contextValues.isIgnoreEmptySlave();
    }

    public DataSourceWrapper newDataSourceWrapper() {
        DataSourceWrapper dataSourceWrapper = new DataSourceWrapper();
        dataSourceWrapper.contextValues = contextValues;
        dataSourceWrapper.dataSource = dataSource;
        dataSourceWrapper.isMaster = isMaster;
        dataSourceWrapper.slaveName = slaveName;
        dataSourceWrapper.isAutoCloseConnection = isAutoCloseConnection;
        return dataSourceWrapper;
    }

    public DataSourceWrapper newDataSourceWrapper(ContextContainer contextValues) {
        DataSourceWrapper dataSourceWrapper = new DataSourceWrapper();
        dataSourceWrapper.contextValues = contextValues;
        dataSourceWrapper.dataSource = dataSource;
        dataSourceWrapper.isMaster = isMaster;
        dataSourceWrapper.slaveName = slaveName;
        dataSourceWrapper.isAutoCloseConnection = isAutoCloseConnection;
        return dataSourceWrapper;
    }
}
