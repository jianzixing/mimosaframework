package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.Session;
import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.SessionHolder;
import org.mimosaframework.orm.exception.MimosaException;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;

public class SpringSessionHolder implements SessionHolder {
    private SessionFactory factory;
    private Session session;

    @Override
    public Session getSession(SessionFactory factory) throws MimosaException {
        this.factory = factory;
        /**
         * 判断spring的事务中是否已经注册了事务，如果已经注册了事务
         * 则拿出之前注册好的资源来直接使用，以免重复创建Session
         *
         * 在spring的事务周期中，只会存在一个session这样可以提高事务效率
         */
        SessionHolderResource resource = (SessionHolderResource) TransactionSynchronizationManager.getResource(factory);
        if (resource != null) {
            return resource.session;
        }

        /**
         * 判断spring是否已经开启了事务，如果没开启则直接返回Session，如果
         * 开启了则注册资源
         */
        Session session = factory.openSession();
        if (TransactionSynchronizationManager.isSynchronizationActive()
                && factory.getConfiguration().getTransactionFactory() instanceof SpringTransactionFactory) {
            resource = new SessionHolderResource(session);
            TransactionSynchronizationManager.bindResource(factory, resource);
            TransactionSynchronizationManager
                    .registerSynchronization(new SpringSynchronizationAdapter(resource, factory));
            resource.setSynchronizedWithTransaction(true);
            resource.requested();
        }
        this.session = session;

        return session;
    }

    @Override
    public boolean begin() {
        return false;
    }

    @Override
    public boolean isSessionTransactional(Session session) {
        SessionHolderResource holder = (SessionHolderResource) TransactionSynchronizationManager.getResource(factory);
        return holder != null && holder.getSession() == session;
    }

    @Override
    public boolean end() {
        return false;
    }

    @Override
    public boolean close() {
        /**
         * 如果Spring中已经注册了资源则释放Spring中的事务资源，当spring中的状态
         * 为0时会自动提交，如果没有注册spring的资源则我们自己管理Session，直接
         * 关闭即可，这里的session默认是不开启connection的事务的所以不需要做什么
         */
        SessionHolderResource resource = (SessionHolderResource) TransactionSynchronizationManager.getResource(factory);
        if (resource != null) {
            // 释放spring的事务资源
            resource.released();
        } else if (this.session != null && !this.isSessionTransactional(this.session)) {
            // 如果没有加入到spring的事务中则自己主动关闭session
            try {
                this.session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    class SessionHolderResource extends ResourceHolderSupport {
        private final Session session;

        public SessionHolderResource(Session session) {
            this.session = session;
        }

        public Session getSession() {
            return session;
        }
    }
}
