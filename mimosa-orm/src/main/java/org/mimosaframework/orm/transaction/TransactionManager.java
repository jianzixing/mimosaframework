package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.exception.TransactionException;

public interface TransactionManager {
    void commit() throws TransactionException;

    void rollback() throws TransactionException;
}
