package org.mimosaframework.orm.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.ContextContainer;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.SessionHolder;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.I18n;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDBCTransaction implements Transaction {
    private static final Log logger = LogFactory.getLog(JDBCTransaction.class);
    private TransactionIsolationType it;
    private ContextContainer context;
    private SessionHolder sessionHolder;
    private Connection connection;
    private int callCount = 0;

    public JDBCTransaction(TransactionIsolationType it, ContextContainer context) {
        this.it = it;
        this.context = context;
    }

    public void setSessionHolder(SessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            MimosaDataSource dataSource = this.context.getAnyDataSource();
            connection = dataSource.getConnection(true, null, true);
        }
        return connection;
    }

    @Override
    public void begin() throws SQLException {
        if (this.sessionHolder != null) {
            this.sessionHolder.begin();
        }
        Connection connection = this.getConnection();
        if (connection != null) {
            connection.setAutoCommit(false);
            if (it != null) connection.setTransactionIsolation(it.getCode());
        }
        callCount++;
    }

    @Override
    public void commit() throws SQLException {
        try {
            if (this.callCount == 1) {
                if (this.sessionHolder != null) {
                    this.sessionHolder.end();
                }
                if (this.connection != null && !connection.getAutoCommit()) {
                    this.connection.commit();
                }
            }
        } finally {
            callCount--;
        }
    }

    @Override
    public void rollback() throws SQLException {
        try {
            if (this.callCount == 1) {
                if (this.sessionHolder != null) {
                    this.sessionHolder.end();
                }
                if (this.connection != null && !connection.getAutoCommit()) {
                    this.connection.rollback();
                }
            }
        } finally {
            callCount--;
        }
    }

    @Override
    public void close() throws SQLException {
        try {
            this.callCount = 0;
            if (this.connection != null && !connection.getAutoCommit()) {
                this.connection.setAutoCommit(true);
            }
        } finally {
            this.connection.close();
        }
        if (this.sessionHolder != null) {
            this.sessionHolder.close();
        }
    }
}
