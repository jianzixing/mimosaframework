package org.mimosaframework.orm.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.ContextContainer;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

import java.sql.Connection;
import java.util.*;

public class TransactionManager implements Transaction {
    private static final Log logger = LogFactory.getLog(TransactionManager.class);
    private static final ThreadLocal<Map<ContextContainer, TransactionManager>> CURRENT_TRANS = new ThreadLocal<>();
    private TransactionPropagationType pt;
    private TransactionIsolationType it;
    private Map<MimosaDataSource, TransactionPropagation> props;
    private ContextContainer context;
    private TransactionManager previousTransaction;

    private boolean isRollback = false;
    private boolean isCommit = false;

    public TransactionManager(TransactionPropagationType pt, TransactionIsolationType it, ContextContainer context) {
        this.pt = pt;
        this.it = it;
        this.context = context;
        if (CURRENT_TRANS.get() == null) {
            CURRENT_TRANS.set(new LinkedHashMap<ContextContainer, TransactionManager>());
        }
    }

    public static Transaction getLastTransaction(ContextContainer contextValues) {
        if (CURRENT_TRANS.get() != null) {
            return CURRENT_TRANS.get().get(contextValues);
        }
        return null;
    }

    private void resetLastTransaction() throws TransactionException {
        // 重置最后一个Trans
        if (this.previousTransaction != null) {
            CURRENT_TRANS.get().put(context, this.previousTransaction);
        } else {
            CURRENT_TRANS.get().remove(context);
        }

        Iterator<Map.Entry<MimosaDataSource, TransactionPropagation>> iterator = props.entrySet().iterator();
        while (iterator.hasNext()) {
            TransactionPropagation propagation = iterator.next().getValue();
            if (propagation != null) {
                propagation.close();
            }
        }
    }

    @Override
    public void begin() throws TransactionException {
        // 设置最后一个Trans是自己，如果有的话就把上一个指向自己
        TransactionManager previous = CURRENT_TRANS.get().get(context);
        if (previous != null) {
            this.previousTransaction = previous;
        }
        CURRENT_TRANS.get().put(context, this);

        List<MimosaDataSource> ds = this.context.getGlobalDataSource();
        if (logger.isDebugEnabled()) {
            logger.debug(Messages.get(LanguageMessageFactory.PROJECT, this.getClass(), "check_db_size", "" + ds.size()));
        }

        if (ds == null) {
            throw new TransactionException(Messages.get(LanguageMessageFactory.PROJECT, this.getClass(), "create_trans_fail"));
        }

        for (MimosaDataSource dataSource : ds) {
            if (props == null) props = new LinkedHashMap<>();
            TransactionPropagation propagation = TransactionPropagationFactory.getPropagation(this.previousTransaction, pt, it);
            propagation.setDataSource(dataSource);
            props.put(dataSource, propagation);
        }

        Iterator<Map.Entry<MimosaDataSource, TransactionPropagation>> iterator = props.entrySet().iterator();
        while (iterator.hasNext()) {
            TransactionPropagation propagation = iterator.next().getValue();
//            if (propagation.getTransaction() != null) {
//                propagation.getTransaction().begin();
//            }
        }
    }

    @Override
    public void commit() throws TransactionException {
        if (!this.isRollback) {
            this.isCommit = true;
            Iterator<Map.Entry<MimosaDataSource, TransactionPropagation>> iterator = props.entrySet().iterator();
            while (iterator.hasNext()) {
                TransactionPropagation propagation = iterator.next().getValue();
                if (propagation != null) {
                    propagation.commit();
                }
            }
        }
        this.resetLastTransaction();
    }

    @Override
    public void rollback() throws TransactionException {
        this.isRollback = true;

        if (!this.isCommit) {
            Iterator<Map.Entry<MimosaDataSource, TransactionPropagation>> iterator = props.entrySet().iterator();
            while (iterator.hasNext()) {
                TransactionPropagation propagation = iterator.next().getValue();
                if (propagation != null) {
                    propagation.rollback();
                }
            }
        }
        this.resetLastTransaction();
    }

    @Override
    public void close() throws TransactionException {
        this.resetLastTransaction();
    }

    @Override
    public Connection getConnection(MimosaDataSource dataSource) throws TransactionException {
        TransactionPropagation propagation = props.get(dataSource);
        if (propagation != null) {
            return propagation.getConnection();
        }
        return null;
    }

    public boolean isAutoCommit(MimosaDataSource dataSource) throws TransactionException {
        TransactionPropagation propagation = props.get(dataSource);
        if (propagation != null) {
            return propagation.isAutoCommit();
        }
        return false;
    }
}
