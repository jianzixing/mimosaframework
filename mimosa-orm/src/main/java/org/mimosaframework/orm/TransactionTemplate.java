package org.mimosaframework.orm;

import org.mimosaframework.orm.transaction.TransactionManager;

public interface TransactionTemplate {
    TransactionManager beginTransaction();

    TransactionManager beginTransaction(Object config);
}
