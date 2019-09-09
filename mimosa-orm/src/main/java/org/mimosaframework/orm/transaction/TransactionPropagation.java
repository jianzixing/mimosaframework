package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;

import java.sql.Connection;

public interface TransactionPropagation {

    void setDataSource(MimosaDataSource dataSource) throws TransactionException;

    Connection getConnection() throws TransactionException;

    void commit() throws TransactionException;

    void rollback() throws TransactionException;

    void close() throws TransactionException;

    /**
     * 虚拟标识事物是否存在
     *
     * @return
     */
    boolean isAutoCommit() throws TransactionException;
}
