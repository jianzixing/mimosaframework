package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.UUID;

public class NestedTransactionPropagation implements TransactionPropagation {
    private MimosaDataSource dataSource;
    private TransactionManager previousTransaction;
    private Transaction transaction;
    private Connection connection;
    private boolean isNewCreate = false;
    private Savepoint savepoint;
    private TransactionIsolationType it;

    public NestedTransactionPropagation(TransactionManager previousTransaction, TransactionIsolationType it) {
        this.it = it;
        this.previousTransaction = previousTransaction;
    }

    @Override
    public void setDataSource(MimosaDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws TransactionException {
        if (this.previousTransaction != null) {
            this.connection = this.previousTransaction.getConnection(dataSource);
        }

        if (this.connection == null) {
            try {
                connection = dataSource.getConnection(true, null, false);
                connection.setAutoCommit(false);
                if (it != null) {
                    connection.setTransactionIsolation(it.getCode());
                }
            } catch (SQLException e) {
                throw new TransactionException("创建事务失败", e);
            }
            isNewCreate = true;
        } else {
            if (savepoint == null) {
                try {
                    this.savepoint = connection.setSavepoint(UUID.randomUUID().toString().replaceAll("-", ""));
                } catch (SQLException e) {
                    throw new TransactionException("创建事务保存点失败", e);
                }
                isNewCreate = false;
            }
        }
        return connection;
    }

    @Override
    public void commit() throws TransactionException {
        if (isNewCreate) {
            try {
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new TransactionException("提交新的事物失败", e);
            }
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if (isNewCreate) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new TransactionException("回滚新的事物失败", e);
            }
        } else {
            try {
                if (connection != null && savepoint != null) {
                    connection.rollback(savepoint);
                }
            } catch (SQLException e) {
                throw new TransactionException("回滚到保存点失败", e);
            }
        }
    }

    @Override
    public void close() throws TransactionException {
        if (isNewCreate) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new TransactionException("关闭数据库连接失败", e);
            }
        }
    }

    @Override
    public boolean isAutoCommit() {
        return true;
    }
}
