package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionPropagationType;

public interface TransactionTemplate {
    Transaction beginTransaction() throws TransactionException;

    Transaction createTransaction();

    <T> T execute(TransactionCallback<T> callback) throws TransactionException;

    <T> T execute(TransactionCallback<T> callback, TransactionPropagationType pt) throws TransactionException;

    <T> T execute(TransactionCallback<T> callback, TransactionIsolationType it) throws TransactionException;

    <T> T execute(TransactionCallback<T> callback, TransactionPropagationType pt, TransactionIsolationType it) throws TransactionException;

}
