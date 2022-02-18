package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;
import org.mimosaframework.orm.transaction.TransactionManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class SpringBeanSessionTemplate implements BeanSessionTemplate {
    private SpringMimosaSessionFactory factory;
    private MimosaBeanSessionTemplate sessionTemplate;

    public void setFactory(SpringMimosaSessionFactory factory) {
        this.factory = factory;
        this.sessionTemplate = new MimosaBeanSessionTemplate(factory);
    }

    @Override
    public TransactionManager beginTransaction() {
        return sessionTemplate.beginTransaction();
    }

    @Override
    public TransactionManager beginTransaction(Object config) {
        return sessionTemplate.beginTransaction(config);
    }

    @Override
    public <T> T execute(TransactionExecutor<T> executor) {
        return sessionTemplate.execute(executor);
    }

    @Override
    public <T> T save(T obj) {
        return sessionTemplate.save(obj);
    }

    @Override
    public <T> T saveOrUpdate(T obj) {
        return sessionTemplate.saveOrUpdate(obj);
    }

    @Override
    public <T> void save(List<T> objects) {
        sessionTemplate.save(objects);
    }

    @Override
    public <T> void update(T obj) {
        sessionTemplate.update(obj);
    }

    @Override
    public <T> void update(List<T> objects) {
        sessionTemplate.update(objects);
    }

    @Override
    public long update(Update update) {
        return sessionTemplate.update(update);
    }

    @Override
    public <T> void delete(T obj) {
        sessionTemplate.delete(obj);
    }

    @Override
    public <T> void delete(List<T> objects) {
        sessionTemplate.delete(objects);
    }

    @Override
    public long delete(Delete delete) {
        return sessionTemplate.delete(delete);
    }

    @Override
    public <T> void delete(Class<T> c, Serializable id) {
        sessionTemplate.delete(c, id);
    }

    @Override
    public <T> T get(Class<T> c, Serializable id) {
        return sessionTemplate.get(c, id);
    }

    @Override
    public <T> T get(Query query) {
        return sessionTemplate.get(query);
    }

    @Override
    public <T> List<T> list(Query query) {
        return sessionTemplate.list(query);
    }

    @Override
    public long count(Query query) {
        return sessionTemplate.count(query);
    }

    @Override
    public <T> Paging<T> paging(Query query) {
        return sessionTemplate.paging(query);
    }

    @Override
    public AutoResult calculate(Function function) {
        return sessionTemplate.calculate(function);
    }

    @Override
    public <T> ZipperTable<T> getZipperTable(Class<T> c) {
        return sessionTemplate.getZipperTable(c);
    }

    @Override
    @Deprecated
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        return sessionTemplate.getAutonomously(autonomously);
    }

    @Override
    public AutoResult sql(SQLAutonomously autonomously) {
        return sessionTemplate.sql(autonomously);
    }

    @Override
    @Deprecated
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        return sessionTemplate.getAutonomously(autonomously);
    }

    @Override
    public AutoResult mapper(TAutonomously autonomously) {
        return sessionTemplate.mapper(autonomously);
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        return sessionTemplate.getDataSourceNames(c);
    }


    @Override
    public void close() throws IOException {
        sessionTemplate.close();
    }

    @Override
    public Query query(Class clazz) {
        return sessionTemplate.query(clazz);
    }

    @Override
    public Delete delete(Class clazz) {
        return sessionTemplate.delete(clazz);
    }

    @Override
    public Update update(Class clazz) {
        return sessionTemplate.update(clazz);
    }
}
