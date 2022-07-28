package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.transaction.TransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

public class SpringTransactionManager implements TransactionManager {
    private PlatformTransactionManager transactionManager;
    private TransactionDefinition transactionDefinition;
    private TransactionStatus status;

    public SpringTransactionManager(PlatformTransactionManager transactionManager,
                                    TransactionDefinition transactionDefinition) {
        // todo:如果没有配置spring的事务，这块应该也能使用需要改一下这块
        Assert.notNull(transactionManager, I18n.print("spring_trans_manager_miss"));
        this.transactionManager = transactionManager;
        this.transactionDefinition = transactionDefinition;
        status = transactionManager.getTransaction(transactionDefinition);
    }

    @Override
    public void commit() throws TransactionException {
        this.transactionManager.commit(status);
    }

    @Override
    public void rollback() throws TransactionException {
        this.transactionManager.rollback(status);
    }
}
