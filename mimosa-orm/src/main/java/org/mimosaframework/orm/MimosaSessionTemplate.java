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
import java.util.List;
import java.util.Set;

import static java.lang.reflect.Proxy.newProxyInstance;

public class MimosaSessionTemplate implements SessionTemplate {
    private SessionFactory sessionFactory;
    private final Session sessionAgency;

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
    }

    public MimosaSessionTemplate(SessionFactory sessionFactory) {
        this();
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
        this.checkAllowInnerJoin(query);
        return this.sessionAgency.get(query);
    }

    @Override
    public List<ModelObject> list(Query query) {
        this.checkAllowInnerJoin(query);
        return this.sessionAgency.list(query);
    }

    @Override
    public long count(Query query) {
        this.checkAllowInnerJoin(query);
        return this.sessionAgency.count(query);
    }

    @Override
    public Paging<ModelObject> paging(Query query) {
        this.checkAllowInnerJoin(query);
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

    protected void checkAllowInnerJoin(Query query) {
        Configuration configuration = sessionFactory.getConfiguration();
        if (configuration.allowInnerJoin() == false) {
            Set<Join> joins = ((DefaultQuery) query).getJoins();
            if (joins != null) {
                for (Join j : joins) {
                    if (j.getJoinType() == 1) {
                        throw new IllegalArgumentException(I18n.print("not_allow_inner_join"));
                    }
                }
            }
        }
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

    @Override
    public <T> T execute(TransactionExecutor<T> executor) {
        TransactionManager manager = null;
        try {
            manager = this.beginTransaction();
            T t = executor.execute(manager);
            manager.commit();
            return t;
        } catch (Throwable e) {
            if (manager != null) {
                manager.rollback();
            }
            throw new TransactionException(I18n.print("fail_rollback"), e);
        }
    }

    private class SessionInterceptor implements InvocationHandler {

        public synchronized SessionHolder getSessionHolder() {
            SessionHolder sessionHolder = null;
            Configuration configuration = sessionFactory.getConfiguration();
            TransactionFactory transactionFactory = configuration.getTransactionFactory();
            if (transactionFactory != null) {
                sessionHolder = transactionFactory.newSessionHolder();
            } else {
                sessionHolder = new SimpleSessionHolder();
            }
            return sessionHolder;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SessionHolder sessionHolder = this.getSessionHolder();
            AssistUtils.isNull(sessionFactory, I18n.print("must_set_factory"));
            Session session = sessionHolder.getSession(sessionFactory);
            Object object = null;
            try {
                object = method.invoke(session, args);
                if (sessionHolder == null || !sessionHolder.isSessionTransactional(session)) {
                    // 现在是Session内部没有设置autocommit的状态，以后如果改的话则这里需要重置设置
                    // session.commit(true);
                }
            } catch (Throwable throwable) {
                if (throwable instanceof InvocationTargetException) {
                    throw throwable.getCause();
                }
                throw throwable;
            } finally {
                if (sessionHolder != null) {
                    sessionHolder.close();
                }
            }
            return object;
        }
    }
}
