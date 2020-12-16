package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionPropagationType;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
        this.sessionAgency = (Session) newProxyInstance(
                Session.class.getClassLoader(),
                new Class[]{Session.class},
                new SessionInterceptor());
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
    public Transaction beginTransaction() throws TransactionException {
        return sessionFactory.beginTransaction();
    }

    @Override
    public Transaction createTransaction() {
        return sessionFactory.createTransaction();
    }

    private <T> T execute(TransactionCallback<T> callback, Transaction transaction) throws TransactionException {
        try {
            T t = callback.invoke(transaction);
            transaction.commit();
            return t;
        } catch (Exception e) {
            transaction.rollback();
            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(I18n.print("fail_rollback"), e);
            }
        }
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback) throws TransactionException {
        Transaction transaction = sessionFactory.beginTransaction();
        return this.execute(callback, transaction);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionPropagationType pt) throws TransactionException {
        Transaction transaction = sessionFactory.beginTransaction(pt);
        return this.execute(callback, transaction);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionIsolationType it) throws TransactionException {
        Transaction transaction = sessionFactory.beginTransaction(it);
        return this.execute(callback, transaction);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionPropagationType pt, TransactionIsolationType it) throws TransactionException {
        Transaction transaction = sessionFactory.beginTransaction(pt, it);
        return this.execute(callback, transaction);
    }

    private class SessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            AssistUtils.notNull(sessionFactory, I18n.print("must_set_factory"));
            Session session = sessionFactory.openSession();
            Object object = null;
            try {
                object = method.invoke(session, args);
            } catch (Throwable throwable) {
                if (throwable instanceof InvocationTargetException) {
                    throw throwable.getCause();
                }
                throw throwable;
            } finally {
                if (session != null) {
                    session.close();
                }
            }
            return object;
        }
    }
}
