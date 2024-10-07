package org.mimosaframework.orm.spring;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;
import org.mimosaframework.orm.transaction.TransactionManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class SpringMimosaSessionTemplate implements SessionTemplate {
    private SpringMimosaSessionFactory factory;
    private MimosaSessionTemplate sessionTemplate;

    public void setFactory(SpringMimosaSessionFactory factory) {
        this.factory = factory;
        this.sessionTemplate = new MimosaSessionTemplate();
        this.sessionTemplate.setSessionFactory(factory);
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
    public ModelObject save(ModelObject obj) {
        return sessionTemplate.save(obj);
    }

    @Override
    public ModelObject saveOrUpdate(ModelObject obj, Object... fields) {
        return sessionTemplate.saveOrUpdate(obj, fields);
    }

    @Override
    public void save(List<ModelObject> objects) {
        sessionTemplate.save(objects);
    }

    @Override
    public int update(ModelObject obj, Object... fields) {
        return sessionTemplate.update(obj, fields);
    }

    @Override
    public int update(List<ModelObject> objects, Object... fields) {
        return sessionTemplate.update(objects, fields);
    }

    @Override
    public int update(Update update) {
        return sessionTemplate.update(update);
    }

    @Override
    public int cover(ModelObject obj) {
        return this.sessionTemplate.cover(obj);
    }

    @Override
    public int delete(ModelObject obj) {
        return sessionTemplate.delete(obj);
    }

    @Override
    public int delete(List<ModelObject> objects) {
        return sessionTemplate.delete(objects);
    }

    @Override
    public int delete(Delete delete) {
        return sessionTemplate.delete(delete);
    }

    @Override
    public int delete(Class c, Serializable id) {
        return sessionTemplate.delete(c, id);
    }

    @Override
    public ModelObject get(Class c, Serializable id) {
        return sessionTemplate.get(c, id);
    }

    @Override
    public ModelObject get(Query query) {
        return sessionTemplate.get(query);
    }

    @Override
    public List<ModelObject> list(Query query) {
        return sessionTemplate.list(query);
    }

    @Override
    public long count(Query query) {
        return sessionTemplate.count(query);
    }

    @Override
    public Paging<ModelObject> paging(Query query) {
        return sessionTemplate.paging(query);
    }

    @Override
    public ZipperTable<ModelObject> getZipperTable(Class c) {
        return sessionTemplate.getZipperTable(c);
    }

    @Override
    public AutoResult calculate(Function function) {
        return sessionTemplate.calculate(function);
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
