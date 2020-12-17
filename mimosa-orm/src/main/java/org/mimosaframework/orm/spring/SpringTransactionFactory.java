package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionFactory;
import org.mimosaframework.orm.transaction.TransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

public class SpringTransactionFactory implements TransactionFactory {
    private PlatformTransactionManager transactionManager;

    public SpringTransactionFactory() {
    }

    public SpringTransactionFactory(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Transaction newTransaction(DataSource dataSource) {
        return new SpringTransaction(dataSource);
    }

    @Override
    public TransactionManager newTransactionManager(SessionFactory sessionFactory, Object config) {
        if (config instanceof TransactionDefinition) {
            return new SpringTransactionManager(transactionManager, (TransactionDefinition) config);
        }
        return new SpringTransactionManager(transactionManager, null);
    }
}
