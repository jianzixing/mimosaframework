package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionIsolationType;

import java.sql.SQLException;

public interface TransactionTemplate {
    Transaction beginTransaction() throws SQLException;

    Transaction createTransaction();

    <T> T execute(TransactionCallback<T> callback) throws Exception;

    <T> T execute(TransactionCallback<T> callback, TransactionIsolationType it) throws Exception;

}
