package org.mimosaframework.orm;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;
import org.mimosaframework.orm.platform.PlatformFactory;
import org.mimosaframework.orm.platform.SimpleTemplate;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionManager;
import org.mimosaframework.orm.transaction.TransactionPropagationType;

import java.util.List;

public class MimosaSessionFactory implements SessionFactory {
    private ContextContainer context;
    private Session currentSession;

    public MimosaSessionFactory(ContextContainer context) {
        this.context = context;
    }

    @Override
    public Session openSession() throws MimosaException {
        Session session = new SessionAgency(this.context);
        currentSession = session;
        return session;
    }

    @Override
    public Session getCurrentSession() throws MimosaException {
        return currentSession;
    }

    @Override
    public Transaction beginTransaction() throws TransactionException {
        TransactionManager manager = new TransactionManager(TransactionPropagationType.PROPAGATION_REQUIRED,
                null,
                this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction beginTransaction(TransactionPropagationType pt) throws TransactionException {
        TransactionManager manager = new TransactionManager(pt,
                null,
                this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction beginTransaction(TransactionIsolationType it) throws TransactionException {
        TransactionManager manager = new TransactionManager(TransactionPropagationType.PROPAGATION_REQUIRED,
                it,
                this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction beginTransaction(TransactionPropagationType pt, TransactionIsolationType it) throws TransactionException {
        TransactionManager manager = new TransactionManager(pt,
                it,
                this.context);
        manager.begin();
        return manager;
    }

    @Override
    public Transaction createTransaction() {
        TransactionManager manager = new TransactionManager(TransactionPropagationType.PROPAGATION_REQUIRED,
                null,
                this.context);
        return manager;
    }

    @Override
    public Transaction createTransaction(TransactionPropagationType pt) {
        TransactionManager manager = new TransactionManager(pt,
                null,
                this.context);
        return manager;
    }

    @Override
    public Transaction createTransaction(TransactionIsolationType it) {
        TransactionManager manager = new TransactionManager(TransactionPropagationType.PROPAGATION_REQUIRED,
                it,
                this.context);
        return manager;
    }

    @Override
    public Transaction createTransaction(TransactionPropagationType pt, TransactionIsolationType it) {
        TransactionManager manager = new TransactionManager(pt,
                it,
                this.context);
        return manager;
    }

    @Override
    public List<FactoryBuilder> getAuxFactoryBuilder() {
        return this.context.getAuxFactoryBuilder();
    }

    @Override
    public SimpleTemplate getSimpleTemplate(String dsname) {
        ActionDataSourceWrapper wrapper = context.getDefaultDataSourceWrapper(true);
        wrapper.setAutoCloseConnection(true);
        if (StringTools.isNotEmpty(dsname)) {
            MimosaDataSource ds = context.getDataSourceByName(dsname);
            if (ds == null) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        MimosaSessionFactory.class, "not_found_ds", dsname));
            }
            wrapper.setDataSource(ds);
        }
        return PlatformFactory.getPlatformSimpleSession(wrapper);
    }
}
