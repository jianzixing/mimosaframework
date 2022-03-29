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
    public <T> int update(T obj) {
        return sessionTemplate.update(obj);
    }

    @Override
    public <T> int updates(List<T> objects) {
        return sessionTemplate.updates(objects);
    }

    @Override
    public <T> int delete(T obj) {
        return sessionTemplate.delete(obj);
    }

    @Override
    public <T> int deletes(List<T> objects) {
        return sessionTemplate.deletes(objects);
    }

    @Override
    public <T> int delete(Class<T> c, Serializable id) {
        return sessionTemplate.delete(c, id);
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
    public Query buildQuery(Class clazz) {
        return sessionTemplate.buildQuery(clazz);
    }

    @Override
    public Delete buildDelete(Class clazz) {
        return sessionTemplate.buildDelete(clazz);
    }

    @Override
    public Update buildUpdate(Class clazz) {
        return sessionTemplate.buildUpdate(clazz);
    }
}
