package org.mimosaframework.orm.transaction;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class RequiredTransactionPropagation implements TransactionPropagation {
    private MimosaDataSource dataSource;
    private boolean previousIsAutoCommit = false;
    private Connection connection;
    private TransactionIsolationType it;
    private TransactionManager previousTransaction;

    public RequiredTransactionPropagation(TransactionManager previousTransaction, TransactionIsolationType it) {
        this.it = it;
        this.previousTransaction = previousTransaction;
    }

    @Override
    public void setDataSource(MimosaDataSource dataSource) throws TransactionException {
        this.dataSource = dataSource;
        if (previousTransaction != null) {
            previousIsAutoCommit = previousTransaction.isAutoCommit(dataSource);
        }
    }

    @Override
    public Connection getConnection() throws TransactionException {
        if (previousIsAutoCommit && this.previousTransaction != null) {
            return this.previousTransaction.getConnection(dataSource);
        } else {
            if (connection == null) {
                try {
                    connection = dataSource.getConnection(true, null, false);
                    connection.setAutoCommit(false);
                    if (it != null) {
                        connection.setTransactionIsolation(it.getCode());
                    }
                } catch (SQLException e) {
                    throw new TransactionException(Messages.get(LanguageMessageFactory.PROJECT,
                            RequiredTransactionPropagation.class, "create_trans_fail"), e);
                }
            }
            return connection;
        }
    }

    @Override
    public void commit() throws TransactionException {
        if (!previousIsAutoCommit && connection != null) {
            try {
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new TransactionException(Messages.get(LanguageMessageFactory.PROJECT,
                        RequiredTransactionPropagation.class, "submit_trans_fail"), e);
            }
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if (!previousIsAutoCommit && connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new TransactionException(Messages.get(LanguageMessageFactory.PROJECT,
                        RequiredTransactionPropagation.class, "rollback_trans_fail"), e);
            }
        }
    }

    @Override
    public void close() throws TransactionException {
        if (!previousIsAutoCommit && connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new TransactionException(Messages.get(LanguageMessageFactory.PROJECT,
                        RequiredTransactionPropagation.class, "close_db_fail"), e);
            }
        }
    }

    @Override
    public boolean isAutoCommit() throws TransactionException {
        return true;
    }
}
