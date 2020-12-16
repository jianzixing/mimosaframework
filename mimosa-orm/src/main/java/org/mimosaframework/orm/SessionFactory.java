package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionIsolationType;

import java.sql.SQLException;

public interface SessionFactory {

    Session openSession() throws MimosaException;

    Session getCurrentSession() throws MimosaException;

    Transaction beginTransaction() throws SQLException;

    Transaction beginTransaction(TransactionIsolationType it) throws SQLException;

    Transaction createTransaction();

    Transaction createTransaction(TransactionIsolationType it);
}
