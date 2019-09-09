package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;

import java.sql.Connection;

public class MandatoryTransactionPropagation implements TransactionPropagation {
    private MimosaDataSource dataSource;
    private TransactionIsolationType it;
    private TransactionManager previousTransaction;

    public MandatoryTransactionPropagation(TransactionManager previousTransaction, TransactionIsolationType it) {
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
            return this.previousTransaction.getConnection(dataSource);
        } else {
            throw new TransactionException("当前传播等级要求必须有外层事务");
        }
    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isAutoCommit() throws TransactionException {
        return this.previousTransaction.isAutoCommit(dataSource);
    }
}
