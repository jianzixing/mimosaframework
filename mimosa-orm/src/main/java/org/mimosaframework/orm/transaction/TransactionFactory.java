package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.transaction.Transaction;

import java.sql.Connection;

public interface TransactionFactory {
    Transaction newTransaction(Connection connection);
}
