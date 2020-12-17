package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.SessionHolder;
import org.mimosaframework.orm.SimpleSessionHolder;

import javax.sql.DataSource;

public class DefaultTransactionFactory implements TransactionFactory {
    @Override
    public JDBCTransaction newTransaction(DataSource dataSource) {
        return new JDBCTransaction(dataSource, TransactionManagerUtils.getTransIsolation());
    }

    @Override
    public TransactionManager newTransactionManager(SessionFactory sessionFactory, Object config) {
        if (config instanceof TransactionIsolationType) {
            TransactionManagerUtils.setTransIsolation((TransactionIsolationType) config);
        }
        return new DefaultTransactionManager(sessionFactory);
    }

    @Override
    public SessionHolder newSessionHolder() {
        return new SimpleSessionHolder();
    }
}
