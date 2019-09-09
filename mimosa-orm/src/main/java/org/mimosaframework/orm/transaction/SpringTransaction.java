package org.mimosaframework.orm.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringTransaction implements Transaction {
    private static final Log logger = LogFactory.getLog(SpringTransaction.class);
    private final DataSource dataSource;
    private Connection connection;
    private boolean isConnectionTransactional;
    private boolean autoCommit;

    public SpringTransaction(DataSource dataSource) {
        Assert.notNull(dataSource, "No DataSource specified");
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        if (this.connection == null) {
            try {
                this.openConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return this.connection;
    }

    @Override
    public Connection getConnection(MimosaDataSource dataSource) throws TransactionException {
        return null;
    }

    @Override
    public void begin() throws TransactionException {

    }

    private void openConnection() throws SQLException {
        this.connection = DataSourceUtils.getConnection(this.dataSource);
        this.autoCommit = this.connection.getAutoCommit();
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);
        if (logger.isDebugEnabled()) {
            logger.debug("JDBC Connection [" + this.connection + "] will" + (this.isConnectionTransactional ? " " : " not ") + "be managed by Spring");
        }

        if (!this.isConnectionTransactional) {
            this.connection.setAutoCommit(false);
            this.autoCommit = false;
        }
    }

    public void commit() throws TransactionException {
        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            if (logger.isDebugEnabled()) {
                logger.debug("Committing JDBC Connection [" + this.connection + "]");
            }

            try {
                this.connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void rollback() throws TransactionException {
        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            if (logger.isDebugEnabled()) {
                logger.debug("Rolling back JDBC Connection [" + this.connection + "]");
            }

            try {
                this.connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void close() throws TransactionException {
        DataSourceUtils.releaseConnection(this.connection, this.dataSource);
    }

    public Integer getTimeout() throws SQLException {
        ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(this.dataSource);
        return holder != null && holder.hasTimeout() ? holder.getTimeToLiveInSeconds() : null;
    }
}
