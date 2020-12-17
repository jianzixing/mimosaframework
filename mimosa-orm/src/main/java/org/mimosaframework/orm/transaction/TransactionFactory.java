package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.SessionFactory;

import javax.sql.DataSource;

public interface TransactionFactory {
    Transaction newTransaction(DataSource dataSource);

    TransactionManager newTransactionManager(SessionFactory sessionFactory, Object config);
}
