package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionFactory;

import javax.sql.DataSource;

public class SpringTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(DataSource dataSource) {
        return new SpringTransaction(dataSource);
    }
}
