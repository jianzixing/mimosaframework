package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

public class NotSupportedTransactionPropagation implements TransactionPropagation {
    private MimosaDataSource dataSource;
    private TransactionIsolationType it;
    private TransactionManager previousTransaction;
    private Connection connection;

    public NotSupportedTransactionPropagation(TransactionManager previousTransaction, TransactionIsolationType it) {
        this.it = it;
        this.previousTransaction = previousTransaction;
    }

    @Override
    public void setDataSource(MimosaDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws TransactionException {
        if (connection == null) {
            try {
                connection = dataSource.getConnection(true, null, false);
            } catch (SQLException e) {
                throw new TransactionException("创建事物失败", e);
            }
        }
        return connection;
    }

    @Override
    public void commit() throws TransactionException {

    }

    @Override
    public void rollback() throws TransactionException {

    }

    @Override
    public void close() throws TransactionException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new TransactionException("关闭数据库连接失败", e);
            }
        }
    }

    @Override
    public boolean isAutoCommit() throws TransactionException {
        return false;
    }
}