package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.SessionHolder;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.I18n;

import java.util.Collection;
import java.util.List;

public class DefaultTransactionManager implements TransactionManager {
    private SessionFactory sessionFactory;
    private boolean isCommit = false;
    private boolean isRollback = false;

    public DefaultTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        TransactionManagerUtils.register(this);
    }

    @Override
    public void commit() throws TransactionException {
        if (this.isRollback) {
            throw new IllegalStateException(I18n.print("trans_is_rollback"));
        }
        if (TransactionManagerUtils.isMarkRollback()) {
            TransactionManagerUtils.release(this);
            throw new IllegalStateException(I18n.print("trans_is_mark_rollback"));
        }
        try {
            if (TransactionManagerUtils.countTransactionManager() == 1
                    && TransactionManagerUtils.hasTransactionManager(this)) {
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

                Collection<SessionHolder> sessionHolders = TransactionManagerUtils.getSessionHolders();
                if (sessionHolders != null) {
                    TransactionManagerUtils.clearSessionHolders();
                    for (SessionHolder sessionHolder : sessionHolders) {
                        if (sessionHolder != null) {
                            sessionHolder.close();
                        }
                    }
                }
                TransactionManagerUtils.clearMarkRollback();
                TransactionManagerUtils.clearTransIsolation();
            }
        } finally {
            TransactionManagerUtils.release(this);
        }
        this.isCommit = true;
    }

    @Override
    public void rollback() throws TransactionException {
        if (this.isCommit) {
            throw new IllegalStateException(I18n.print("trans_is_commit"));
        }
        if (TransactionManagerUtils.isMarkRollback()
                && TransactionManagerUtils.countTransactionManager() != 1
                && TransactionManagerUtils.hasTransactionManager(this)) {
            TransactionManagerUtils.release(this);
            throw new IllegalStateException(I18n.print("trans_is_mark_rollback"));
        }
        try {
            if (TransactionManagerUtils.countTransactionManager() == 1
                    && TransactionManagerUtils.hasTransactionManager(this)) {
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

                Collection<SessionHolder> sessionHolders = TransactionManagerUtils.getSessionHolders();
                TransactionManagerUtils.clearSessionHolders();
                for (SessionHolder sessionHolder : sessionHolders) {
                    if (sessionHolder != null) {
                        sessionHolder.close();
                    }
                }
                TransactionManagerUtils.clearMarkRollback();
                TransactionManagerUtils.clearTransIsolation();
            }
        } finally {
            TransactionManagerUtils.release(this);
        }
        this.isRollback = true;
        TransactionManagerUtils.markRollback();
    }
}
