package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.JDBCTransaction;

import java.sql.SQLException;

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
    public Transaction beginTransaction() throws SQLException {
        JDBCTransaction manager = new JDBCTransaction(null, this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction beginTransaction(TransactionIsolationType it) throws SQLException {
        JDBCTransaction manager = new JDBCTransaction(it, this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction createTransaction() {
        JDBCTransaction manager = new JDBCTransaction(null, this.context);
        return manager;
    }

    @Override
    public Transaction createTransaction(TransactionIsolationType it) {
        JDBCTransaction manager = new JDBCTransaction(it, this.context);
        return manager;
    }
}
