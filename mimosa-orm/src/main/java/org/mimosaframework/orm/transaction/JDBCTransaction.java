package org.mimosaframework.orm.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.SessionHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCTransaction implements Transaction {
    private static final Log logger = LogFactory.getLog(JDBCTransaction.class);
    private TransactionIsolationType it;
    private DataSource dataSource;
    private SessionHolder sessionHolder;
    private Connection connection;
    private boolean support = true;

    public JDBCTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JDBCTransaction(DataSource dataSource, boolean support) {
        this.dataSource = dataSource;
        this.support = support;
    }

    public JDBCTransaction(TransactionIsolationType it, DataSource dataSource) {
        this.it = it;
        this.dataSource = dataSource;
    }

    public void setSessionHolder(SessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (this) {
                if (connection == null) {
                    connection = dataSource.getConnection();
                }
            }
        }
        return connection;
    }

    @Override
    public void begin() throws SQLException {
        if (support) {
            if (this.sessionHolder != null) {
                this.sessionHolder.begin();
            }
            Connection connection = this.getConnection();
            if (connection != null) {
                connection.setAutoCommit(false);
                if (it != null) connection.setTransactionIsolation(it.getCode());
            }
        }
    }

    @Override
    public void commit() throws SQLException {
        if (support) {
            if (this.sessionHolder != null) {
                this.sessionHolder.end();
            }
            if (this.connection != null && !connection.getAutoCommit()) {
                this.connection.commit();
            }
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (support) {
            if (this.sessionHolder != null) {
                this.sessionHolder.end();
            }
            if (this.connection != null && !connection.getAutoCommit()) {
                this.connection.rollback();
            }
        }
    }

    @Override
    public void close() throws SQLException {
        try {
            if (support) {
                if (this.connection != null && !connection.getAutoCommit()) {
                    this.connection.setAutoCommit(true);
                }
            }
        } finally {
            this.connection.close();
            this.connection = null;
        }
        if (this.sessionHolder != null && support) {
            this.sessionHolder.close();
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return this.connection.getNetworkTimeout();
    }
}
