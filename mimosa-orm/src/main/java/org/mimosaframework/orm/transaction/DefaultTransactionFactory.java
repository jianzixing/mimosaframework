package org.mimosaframework.orm.transaction;

import javax.sql.DataSource;

public class DefaultTransactionFactory implements TransactionFactory {
    @Override
    public JDBCTransaction newTransaction(DataSource dataSource) {
        return new JDBCTransaction(dataSource);
    }
}
