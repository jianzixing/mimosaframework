package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.SessionFactory;

import javax.sql.DataSource;

public class DefaultTransactionFactory implements TransactionFactory {
    @Override
    public JDBCTransaction newTransaction(DataSource dataSource) {
        return new JDBCTransaction(dataSource);
    }

    @Override
    public TransactionManager newTransactionManager(SessionFactory sessionFactory, Object config) {
        return new DefaultTransactionManager(sessionFactory);
    }
}
