package org.mimosaframework.orm.transaction;

import javax.sql.DataSource;

public interface TransactionFactory {
    Transaction newTransaction(DataSource dataSource);
}
