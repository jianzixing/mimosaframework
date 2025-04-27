package org.mimosaframework.orm.spring;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.transaction.TransactionManager;

import java.io.IOException;
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
    public <T> T saveOrUpdate(T obj, Object... fields) {
        return sessionTemplate.saveOrUpdate(obj, fields);
    }

    @Override
    public <T> T saveOrUpdateSelective(T obj, FieldFunction<T>... fields) {
        return sessionTemplate.saveOrUpdateSelective(obj, fields);
    }

    @Override
    public <T> void save(List<T> objects) {
        sessionTemplate.save(objects);
    }

    @Override
    public int update(Update update) {
        return sessionTemplate.update(update);
    }

    @Override
    public <T> int update(T obj, Object... fields) {
        return sessionTemplate.update(obj, fields);
    }

    @Override
    public <T> int updateSelective(T obj, FieldFunction<T>... fields) {
        return sessionTemplate.update(obj, fields);
    }

    @Override
    public <T> int update(List<T> objects, Object... fields) {
        return sessionTemplate.update(objects, fields);
    }

    @Override
    public <T> int updateSelective(List<T> objects, FieldFunction<T>... fields) {
        return sessionTemplate.update(objects, fields);
    }

    @Override
    public <T> int modify(T obj) {
        return sessionTemplate.modify(obj);
    }

    @Override
    public <T> int delete(T obj) {
        return sessionTemplate.delete(obj);
    }

    @Override
    public <T> int delete(List<T> objects) {
        return sessionTemplate.delete(objects);
    }

    @Override
    public <T> int delete(Class<T> c, Object id) {
        return sessionTemplate.delete(c, id);
    }

    @Override
    public <T> T get(Class<T> c, Object id) {
        return sessionTemplate.get(c, id);
    }

    @Override
    public <T> T get(Query<T> query) {
        return sessionTemplate.get(query);
    }

    @Override
    public <T> List<T> list(Query<T> query) {
        return sessionTemplate.list(query);
    }

    @Override
    public long count(Query query) {
        return sessionTemplate.count(query);
    }

    @Override
    public <T> Paging<T> paging(Query<T> query) {
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
    public AutoResult getAutonomously(Sql autonomously) throws Exception {
        return sessionTemplate.getAutonomously(autonomously);
    }

    @Override
    public AutoResult sql(Sql autonomously) {
        return sessionTemplate.sql(autonomously);
    }

    @Override
    @Deprecated
    public AutoResult getAutonomously(Mapper autonomously) throws Exception {
        return sessionTemplate.getAutonomously(autonomously);
    }

    @Override
    public AutoResult mapper(Mapper autonomously) {
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
