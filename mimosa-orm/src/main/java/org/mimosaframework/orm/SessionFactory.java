package org.mimosaframework.orm;

import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.builder.Builder;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.platform.SimpleTemplate;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionPropagationType;

import java.util.List;

public interface SessionFactory {

    Session openSession() throws MimosaException;

    Session getCurrentSession() throws MimosaException;

    Transaction beginTransaction() throws TransactionException;

    Transaction beginTransaction(TransactionPropagationType pt) throws TransactionException;

    Transaction beginTransaction(TransactionIsolationType it) throws TransactionException;

    Transaction beginTransaction(TransactionPropagationType pt, TransactionIsolationType it) throws TransactionException;

    Transaction createTransaction();

    Transaction createTransaction(TransactionPropagationType pt);

    Transaction createTransaction(TransactionIsolationType it);

    Transaction createTransaction(TransactionPropagationType pt, TransactionIsolationType it);

    List<FactoryBuilder> getAuxFactoryBuilder();

    SimpleTemplate getSimpleTemplate(String dsname);
}
