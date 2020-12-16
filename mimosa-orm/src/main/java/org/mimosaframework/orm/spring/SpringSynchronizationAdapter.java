package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.SessionFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;

public class SpringSynchronizationAdapter extends TransactionSynchronizationAdapter {

    private final SpringSessionHolder.SessionHolderResource holder;
    private final SessionFactory sessionFactory;

    private boolean holderActive = true;

    public SpringSynchronizationAdapter(SpringSessionHolder.SessionHolderResource holder,
                                        SessionFactory sessionFactory) {
        this.holder = holder;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void suspend() {
        if (this.holderActive) {
            TransactionSynchronizationManager.unbindResource(this.sessionFactory);
        }
    }

    @Override
    public void resume() {
        if (this.holderActive) {
            TransactionSynchronizationManager.bindResource(this.sessionFactory, this.holder);
        }
    }

    @Override
    public void beforeCommit(boolean readOnly) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 提交事务
            // this.holder.getSession().commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeCompletion() {
        if (!this.holder.isOpen()) {
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            this.holderActive = false;
            try {
                this.holder.getSession().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(int status) {
        if (this.holderActive) {
            TransactionSynchronizationManager.unbindResourceIfPossible(sessionFactory);
            this.holderActive = false;
            try {
                this.holder.getSession().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.holder.reset();
    }
}
