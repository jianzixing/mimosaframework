package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.exception.TransactionException;

import java.util.List;

public class DefaultTransactionManager implements TransactionManager {
    private SessionFactory sessionFactory;

    public DefaultTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        TransactionManagerUtils.register(this);
    }

    @Override
    public void commit() throws TransactionException {
        List<Transaction> transactions = TransactionManagerUtils.getTransactions();
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                try {
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        TransactionManagerUtils.remove(this);
    }

    @Override
    public void rollback() throws TransactionException {
        List<Transaction> transactions = TransactionManagerUtils.getTransactions();
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                try {
                    transaction.rollback();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        TransactionManagerUtils.remove(this);
    }
}
