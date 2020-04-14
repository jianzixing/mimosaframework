package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.I18n;

import java.sql.Connection;
import java.sql.SQLException;

public class NeverTransactionPropagation implements TransactionPropagation {
    private MimosaDataSource dataSource;
    private TransactionIsolationType it;
    private TransactionManager previousTransaction;
    private Connection connection;

    public NeverTransactionPropagation(TransactionManager previousTransaction, TransactionIsolationType it) {
        this.it = it;
        this.previousTransaction = previousTransaction;
    }

    @Override
    public void setDataSource(MimosaDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws TransactionException {
        if (this.previousTransaction != null && this.previousTransaction.isAutoCommit(dataSource)) {
            throw new TransactionException(I18n.print("found_trans"));
        }
        if (connection == null) {
            try {
                connection = dataSource.getConnection(true, null, true);
            } catch (SQLException e) {
                throw new TransactionException(I18n.print("create_trans_fail"), e);
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
                throw new TransactionException(I18n.print("close_trans_fail"), e);
            }
        }
    }

    @Override
    public boolean isAutoCommit() throws TransactionException {
        return false;
    }
}
