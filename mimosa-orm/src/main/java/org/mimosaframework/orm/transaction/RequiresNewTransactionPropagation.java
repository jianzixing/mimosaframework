package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

public class RequiresNewTransactionPropagation implements TransactionPropagation {
    private MimosaDataSource dataSource;
    private TransactionIsolationType it;
    private Connection connection;
    private TransactionManager previousTransaction;

    public RequiresNewTransactionPropagation(TransactionManager previousTransaction, TransactionIsolationType it) {
        this.it = it;
        this.previousTransaction = previousTransaction;
    }

    @Override
    public void setDataSource(MimosaDataSource dataSource) throws TransactionException {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws TransactionException {
        if (connection == null) {
            try {
                connection = dataSource.getConnection(true, null, false);
                connection.setAutoCommit(false);
                if (it != null) {
                    connection.setTransactionIsolation(it.getCode());
                }
            } catch (SQLException e) {
                throw new TransactionException("创建事物失败", e);
            }
        }
        return connection;
    }

    @Override
    public void commit() throws TransactionException {
        if (connection != null) {
            try {
                if (connection.isClosed()) {
                    throw new TransactionException("提交事物时数据库连接被关闭");
                }
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new TransactionException("提交事物失败", e);
            }
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if (connection != null) {
            try {
                if (connection.isClosed()) {
                    throw new TransactionException("回滚事物时数据库连接被关闭");
                }
                connection.rollback();
            } catch (SQLException e) {
                throw new TransactionException("回滚事物失败", e);
            }
        }
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
        return true;
    }
}
