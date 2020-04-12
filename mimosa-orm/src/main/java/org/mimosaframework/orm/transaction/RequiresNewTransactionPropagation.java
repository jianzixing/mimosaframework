package org.mimosaframework.orm.transaction;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.I18n;

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
                throw new TransactionException(I18n.print("create_trans_fail"), e);
            }
        }
        return connection;
    }

    @Override
    public void commit() throws TransactionException {
        if (connection != null) {
            try {
                if (connection.isClosed()) {
                    throw new TransactionException(I18n.print("submit_db_close"));
                }
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new TransactionException(I18n.print("submit_trans_fail"), e);
            }
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if (connection != null) {
            try {
                if (connection.isClosed()) {
                    throw new TransactionException(I18n.print("rollback_trans_db_close"));
                }
                connection.rollback();
            } catch (SQLException e) {
                throw new TransactionException(I18n.print("rollback_trans_fail"), e);
            }
        }
    }

    @Override
    public void close() throws TransactionException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new TransactionException(I18n.print("db_close_fail"), e);
            }
        }
    }

    @Override
    public boolean isAutoCommit() throws TransactionException {
        return true;
    }
}
