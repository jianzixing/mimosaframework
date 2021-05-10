package org.mimosaframework.orm;

import org.mimosaframework.orm.transaction.TransactionManager;

public interface TransactionExecutor<T> {
    T execute(TransactionManager manager) throws Exception;
}
