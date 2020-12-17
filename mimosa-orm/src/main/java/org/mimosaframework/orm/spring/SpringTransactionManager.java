package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.transaction.TransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.sql.SQLException;

public class SpringTransactionManager implements TransactionManager {
    private PlatformTransactionManager transactionManager;
    private TransactionDefinition transactionDefinition;
    private TransactionStatus status;

    public SpringTransactionManager(PlatformTransactionManager transactionManager,
                                    TransactionDefinition transactionDefinition) {
        this.transactionManager = transactionManager;
        this.transactionDefinition = transactionDefinition;
        status = transactionManager.getTransaction(transactionDefinition);
    }

    @Override
    public void commit() throws SQLException {
        this.transactionManager.commit(status);
    }

    @Override
    public void rollback() throws SQLException {
        this.transactionManager.rollback(status);
    }
}
