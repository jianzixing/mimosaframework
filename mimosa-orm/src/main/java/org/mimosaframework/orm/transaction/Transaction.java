package org.mimosaframework.orm.transaction;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;

import java.sql.Connection;

public interface Transaction {

    Connection getConnection(MimosaDataSource dataSource) throws TransactionException;

    void begin() throws TransactionException;

    void commit() throws TransactionException;

    void rollback() throws TransactionException;

    void close() throws TransactionException;
}
