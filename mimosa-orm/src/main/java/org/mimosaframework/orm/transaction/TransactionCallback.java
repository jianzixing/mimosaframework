package org.mimosaframework.orm.transaction;

public interface TransactionCallback<T> {
    T invoke(Transaction transaction) throws Exception;
}
