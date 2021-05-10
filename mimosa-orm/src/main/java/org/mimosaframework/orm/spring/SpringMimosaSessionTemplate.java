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
    public ModelObject saveAndUpdate(ModelObject obj) {
        return sessionTemplate.saveAndUpdate(obj);
    }

    @Override
    public void save(List<ModelObject> objects) {
        sessionTemplate.save(objects);
    }

    @Override
    public void update(ModelObject obj) {
        sessionTemplate.update(obj);
    }

    @Override
    public void update(List<ModelObject> objects) {
        sessionTemplate.update(objects);
    }

    @Override
    public long update(Update update) {
        return sessionTemplate.update(update);
    }

    @Override
    public void delete(ModelObject obj) {
        sessionTemplate.delete(obj);
    }

    @Override
    public void delete(List<ModelObject> objects) {
        sessionTemplate.delete(objects);
    }

    @Override
    public long delete(Delete delete) {
        return sessionTemplate.delete(delete);
    }

    @Override
    public void delete(Class c, Serializable id) {
        sessionTemplate.delete(c, id);
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
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        return sessionTemplate.getAutonomously(autonomously);
    }

    @Override
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        return sessionTemplate.getAutonomously(autonomously);
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
