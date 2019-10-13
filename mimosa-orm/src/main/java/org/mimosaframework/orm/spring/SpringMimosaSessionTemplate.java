package org.mimosaframework.orm.spring;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.auxiliary.*;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionPropagationType;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
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
    public Transaction beginTransaction() throws TransactionException {
        return sessionTemplate.beginTransaction();
    }

    @Override
    public Transaction createTransaction() {
        return sessionTemplate.createTransaction();
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback) throws TransactionException {
        return sessionTemplate.execute(callback);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionPropagationType pt) throws TransactionException {
        return sessionTemplate.execute(callback, pt);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionIsolationType it) throws TransactionException {
        return sessionTemplate.execute(callback, it);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionPropagationType pt, TransactionIsolationType it) throws TransactionException {
        return sessionTemplate.execute(callback, pt, it);
    }

    @Override
    public CacheClient getCacheClient(String group) {
        return sessionTemplate.getCacheClient(group);
    }

    @Override
    public CacheClient getCacheClient(String name, String group) {
        return sessionTemplate.getCacheClient(name, group);
    }

    @Override
    public MQProducer getMQProducer(String group) {
        return sessionTemplate.getMQProducer(group);
    }

    @Override
    public MQProducer getMQProducer(String name, String group) {
        return sessionTemplate.getMQProducer(name, group);
    }

    @Override
    public void registerMQConsumer(MQConsumer consumer) {
        sessionTemplate.registerMQConsumer(consumer);
    }

    @Override
    public void registerMQConsumer(String name, MQConsumer consumer) {
        sessionTemplate.registerMQConsumer(name, consumer);
    }

    @Override
    public SearchEngineClient getSearchEngineClient(String group) {
        return sessionTemplate.getSearchEngineClient(group);
    }

    @Override
    public SearchEngineClient getSearchEngineClient(String name, String group) {
        return sessionTemplate.getSearchEngineClient(name, group);
    }

    @Override
    public <T> T getRPCObject(Class<T> tClass) {
        return sessionTemplate.getRPCObject(tClass);
    }

    @Override
    public <T> T getRPCObject(String name, Class<T> tClass) {
        return sessionTemplate.getRPCObject(name, tClass);
    }

    @Override
    public void registerRPCObject(String name, Object o) {
        sessionTemplate.registerRPCObject(name, o);
    }

    @Override
    public void registerRPCObject(Object o) {
        sessionTemplate.registerRPCObject(o);
    }

    @Override
    public Monitoring getMonitoring(String group) {
        return sessionTemplate.getMonitoring(group);
    }

    @Override
    public Monitoring getMonitoring(String name, String group) {
        return sessionTemplate.getMonitoring(name, group);
    }

    @Override
    public Switch getSwitch(String group) {
        return sessionTemplate.getSwitch(group);
    }

    @Override
    public Switch getSwitch(String name, String group) {
        return sessionTemplate.getSwitch(name, group);
    }

    @Override
    public void notifyChangedSwitch(SwitchChangedListener listener) {
        sessionTemplate.notifyChangedSwitch(listener);
    }

    @Override
    public void notifyChangedSwitch(String name, SwitchChangedListener listener) {
        sessionTemplate.notifyChangedSwitch(name, listener);
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
    public <T> Query<T> query(Class<T> clazz) {
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
