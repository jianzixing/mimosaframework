package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.transaction.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import static java.lang.reflect.Proxy.newProxyInstance;

public class MimosaSessionTemplate implements SessionTemplate {
    private SessionFactory sessionFactory;
    private final Session sessionAgency;
    private SessionHolder sessionHolder;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public MimosaSessionTemplate() {
        SessionInterceptor sessionInterceptor = new SessionInterceptor();
        this.sessionAgency = (Session) newProxyInstance(
                Session.class.getClassLoader(),
                new Class[]{Session.class}, sessionInterceptor);
        if (this.sessionHolder != null) {
            sessionInterceptor.setSessionHolder(this.sessionHolder);
        }
    }

    public MimosaSessionTemplate(SessionFactory sessionFactory) {
        this();
        this.sessionFactory = sessionFactory;
        this.setSessionFactory(sessionFactory);
    }

    public MimosaSessionTemplate(SessionHolder sessionHolder) {
        this();
        this.sessionHolder = sessionHolder;
        this.setSessionFactory(sessionFactory);
    }

    public MimosaSessionTemplate(SessionFactory sessionFactory, SessionHolder sessionHolder) {
        this();
        this.sessionHolder = sessionHolder;
        this.sessionFactory = sessionFactory;
        this.setSessionFactory(sessionFactory);
    }

    @Override
    public ModelObject save(ModelObject obj) {
        return this.sessionAgency.save(obj);
    }

    @Override
    public ModelObject saveAndUpdate(ModelObject obj) {
        return this.sessionAgency.saveAndUpdate(obj);
    }

    @Override
    public void save(List<ModelObject> objects) {
        this.sessionAgency.save(objects);
    }

    @Override
    public void update(ModelObject obj) {
        this.sessionAgency.update(obj);
    }

    @Override
    public void update(List<ModelObject> objects) {
        this.sessionAgency.update(objects);
    }

    @Override
    public long update(Update update) {
        return this.sessionAgency.update(update);
    }

    @Override
    public void delete(ModelObject obj) {
        this.sessionAgency.delete(obj);
    }

    @Override
    public void delete(List<ModelObject> objects) {
        this.sessionAgency.delete(objects);
    }

    @Override
    public long delete(Delete delete) {
        return this.sessionAgency.delete(delete);
    }

    @Override
    public void delete(Class c, Serializable id) {
        this.sessionAgency.delete(c, id);
    }

    @Override
    public ModelObject get(Class c, Serializable id) {
        return this.sessionAgency.get(c, id);
    }

    @Override
    public ModelObject get(Query query) {
        return this.sessionAgency.get(query);
    }

    @Override
    public List<ModelObject> list(Query query) {
        return this.sessionAgency.list(query);
    }

    @Override
    public long count(Query query) {
        return this.sessionAgency.count(query);
    }

    @Override
    public Paging<ModelObject> paging(Query query) {
        return this.sessionAgency.paging(query);
    }

    @Override
    public ZipperTable<ModelObject> getZipperTable(Class c) {
        return this.sessionAgency.getZipperTable(c);
    }

    @Override
    public AutoResult calculate(Function function) {
        return this.sessionAgency.calculate(function);
    }

    @Override
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        return this.sessionAgency.getAutonomously(autonomously);
    }

    @Override
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        return this.sessionAgency.getAutonomously(autonomously);
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        return this.sessionAgency.getDataSourceNames(c);
    }


    @Override
    public Query query(Class clazz) {
        Query query = new DefaultQuery(this, clazz);
        return query;
    }


    @Override
    public Delete delete(Class clazz) {
        Delete delete = new DefaultDelete(this, clazz);
        return delete;
    }

    @Override
    public Update update(Class clazz) {
        Update update = new DefaultUpdate(this, clazz);
        return update;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public TransactionManager beginTransaction() {
        return beginTransaction(null);
    }

    @Override
    public TransactionManager beginTransaction(Object config) {
        Configuration configuration = sessionFactory.getConfiguration();
        TransactionFactory transactionFactory = configuration.getTransactionFactory();
        return transactionFactory.newTransactionManager(sessionFactory, config);
    }

    private class SessionInterceptor implements InvocationHandler {
        private SessionHolder sessionHolder;

        public synchronized void setSessionHolder(SessionHolder sessionHolder) {
            this.sessionHolder = sessionHolder;
        }

        public synchronized SessionHolder getSessionHolder() {
            if (this.sessionHolder == null) {
                this.sessionHolder = new SimpleSessionHolder();
            }
            return sessionHolder;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (this.sessionHolder == null) {
                this.getSessionHolder();
            }
            AssistUtils.notNull(sessionFactory, I18n.print("must_set_factory"));
            Session session = this.sessionHolder.getSession(sessionFactory);
            Object object = null;
            try {
                object = method.invoke(session, args);
                if (this.sessionHolder == null || !this.sessionHolder.isSessionTransactional(session)) {
                    // 现在是Session内部没有设置autocommit的状态，以后如果改的话则这里需要重置设置
                    // session.commit(true);
                }
            } catch (Throwable throwable) {
                if (throwable instanceof InvocationTargetException) {
                    throw throwable.getCause();
                }
                throw throwable;
            } finally {
                if (session != null) {
                    try {
                        session.close();
                    } finally {
                        if (this.sessionHolder != null) {
                            this.sessionHolder.close();
                        }
                    }
                }
            }
            return object;
        }
    }
}
