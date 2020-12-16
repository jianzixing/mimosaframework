package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionManager;
import org.mimosaframework.orm.transaction.TransactionPropagationType;

public class MimosaSessionFactory implements SessionFactory {
    private ContextContainer context;
    private Session currentSession;

    public MimosaSessionFactory(ContextContainer context) {
        this.context = context;
    }

    @Override
    public Session openSession() throws MimosaException {
        Session session = new SessionAgency(this.context);
        currentSession = session;
        return session;
    }

    @Override
    public Session getCurrentSession() throws MimosaException {
        return currentSession;
    }

    @Override
    public Transaction beginTransaction() throws TransactionException {
        TransactionManager manager = new TransactionManager(TransactionPropagationType.PROPAGATION_REQUIRED,
                null,
                this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction beginTransaction(TransactionPropagationType pt) throws TransactionException {
        TransactionManager manager = new TransactionManager(pt,
                null,
                this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction beginTransaction(TransactionIsolationType it) throws TransactionException {
        TransactionManager manager = new TransactionManager(TransactionPropagationType.PROPAGATION_REQUIRED,
                it,
                this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction beginTransaction(TransactionPropagationType pt, TransactionIsolationType it) throws TransactionException {
        TransactionManager manager = new TransactionManager(pt,
                it,
                this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction createTransaction() {
        TransactionManager manager = new TransactionManager(TransactionPropagationType.PROPAGATION_REQUIRED,
                null,
                this.context);
        return manager;
    }

    @Override
    public Transaction createTransaction(TransactionPropagationType pt) {
        TransactionManager manager = new TransactionManager(pt,
                null,
                this.context);
        return manager;
    }

    @Override
    public Transaction createTransaction(TransactionIsolationType it) {
        TransactionManager manager = new TransactionManager(TransactionPropagationType.PROPAGATION_REQUIRED,
                it,
                this.context);
        return manager;
    }

    @Override
    public Transaction createTransaction(TransactionPropagationType pt, TransactionIsolationType it) {
        TransactionManager manager = new TransactionManager(pt,
                it,
                this.context);
        return manager;
    }
}
