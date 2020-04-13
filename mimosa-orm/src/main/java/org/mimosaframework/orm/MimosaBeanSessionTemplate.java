package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MimosaBeanSessionTemplate implements BeanSessionTemplate {
    private MimosaSessionTemplate modelSession = new MimosaSessionTemplate();
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        modelSession.setSessionFactory(sessionFactory);
    }


    @Override
    public <T> T save(T obj) {
        return null;
    }

    @Override
    public <T> T saveAndUpdate(T obj) {
        return null;
    }

    @Override
    public <T> void save(List<T> objects) {

    }

    @Override
    public <T> void update(T obj) {

    }

    @Override
    public <T> void update(List<T> objects) {

    }

    @Override
    public void update(Update update) {

    }

    @Override
    public <T> void delete(T obj) {

    }

    @Override
    public <T> void delete(List<T> objects) {

    }

    @Override
    public void delete(Delete delete) {

    }

    @Override
    public <T> void delete(Class<T> c, Serializable id) {

    }

    @Override
    public <T> T get(Class<T> c, Serializable id) {
        return null;
    }

    @Override
    public <T> T get(Query query) {
        return null;
    }

    @Override
    public <T> List<T> list(Query query) {
        return null;
    }

    @Override
    public long count(Query query) {
        return 0;
    }

    @Override
    public <T> Paging<T> paging(Query query) {
        return null;
    }

    @Override
    public Map<String, Object> calculate(Function function) {
        return null;
    }

    @Override
    public <T> ZipperTable<T> getZipperTable(Class<T> c) {
        return null;
    }


    @Override
    public Query query(Class clazz) {
        Query query = new DefaultQuery(clazz);
        return query;
    }


    @Override
    public Delete delete(Class clazz) {
        Delete delete = new DefaultDelete(clazz);
        return delete;
    }

    @Override
    public Update update(Class clazz) {
        Update update = new DefaultUpdate(clazz);
        return update;
    }

    @Override
    public void close() throws IOException {
        modelSession.close();
    }
}
