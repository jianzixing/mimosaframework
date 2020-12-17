package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.SessionHolder;
import org.mimosaframework.orm.exception.TransactionException;

import java.util.Collection;
import java.util.List;

public class DefaultTransactionManager implements TransactionManager {
    private SessionFactory sessionFactory;

    public DefaultTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        TransactionManagerUtils.register(this);
    }

    @Override
    public void commit() throws TransactionException {
        if (TransactionManagerUtils.countTransactionManager() == 1) {
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

            Collection<SessionHolder> sessionHolders = TransactionManagerUtils.getSessionHolders();
            if (sessionHolders != null) {
                TransactionManagerUtils.clearSessionHolders();
                for (SessionHolder sessionHolder : sessionHolders) {
                    if (sessionHolder != null) {
                        sessionHolder.close();
                    }
                }
            }
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if (TransactionManagerUtils.countTransactionManager() == 1) {
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

            Collection<SessionHolder> sessionHolders = TransactionManagerUtils.getSessionHolders();
            TransactionManagerUtils.clearSessionHolders();
            for (SessionHolder sessionHolder : sessionHolders) {
                if (sessionHolder != null) {
                    sessionHolder.close();
                }
            }
        }
    }
}
