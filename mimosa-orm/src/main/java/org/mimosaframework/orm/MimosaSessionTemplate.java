package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.auxiliary.*;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionPropagationType;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.reflect.Proxy.newProxyInstance;

public class MimosaSessionTemplate implements SessionTemplate {
    private SessionFactory sessionFactory;
    private final Session sessionAgency;
    private Map<String, FactoryBuilder> factoryBuilderMap;

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
    public <T> Query<T> query(Class<T> clazz) {
        Query<T> query = new DefaultQuery<T>(clazz, this);
        return query;
    }


    @Override
    public Delete delete(Class clazz) {
        Delete delete = new DefaultDelete(clazz, this);
        return delete;
    }

    @Override
    public Update update(Class clazz) {
        Update update = new DefaultUpdate(clazz, this);
        return update;
    }

    @Override
    public void close() throws IOException {

    }

    private <T> T setSelf(AuxiliaryClient client) {
        client.setSession(this);
        return (T) client;
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
                throw new TransactionException("执行事物失败并回滚", e);
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

    private synchronized void initFactoryBuilder() {
        List<FactoryBuilder> fbs = sessionFactory.getAuxFactoryBuilder();
        try {
            if (fbs != null) {
                if (this.factoryBuilderMap == null) {
                    this.factoryBuilderMap = new ConcurrentHashMap<>();
                }
            }

            if (fbs != null) {
                for (FactoryBuilder factoryBuilder : fbs) {
                    setFactoryBuilderType(factoryBuilder);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("创建辅助工具工厂类失败", e);
        }
    }

    private void setFactoryBuilderType(FactoryBuilder factoryBuilder) {
        String name = factoryBuilder.getName();
        if (StringTools.isEmpty(name)) name = "DEFAULT";
        if (factoryBuilder instanceof CacheFactoryBuilder) {
            this.factoryBuilderMap.put("C_" + name, factoryBuilder);
            if (this.factoryBuilderMap.get("C_DEFAULT") == null) {
                this.factoryBuilderMap.put("C_DEFAULT", factoryBuilder);
            }
        }
        if (factoryBuilder instanceof MQFactoryBuilder) {
            this.factoryBuilderMap.put("MQ_" + name, factoryBuilder);
            if (this.factoryBuilderMap.get("MQ_DEFAULT") == null) {
                this.factoryBuilderMap.put("MQ_DEFAULT", factoryBuilder);
            }
        }
        if (factoryBuilder instanceof SearchEngineFactoryBuilder) {
            this.factoryBuilderMap.put("SE_" + name, factoryBuilder);
            if (this.factoryBuilderMap.get("SE_DEFAULT") == null) {
                this.factoryBuilderMap.put("SE_DEFAULT", factoryBuilder);
            }
        }
        if (factoryBuilder instanceof RPCFactoryBuilder) {
            this.factoryBuilderMap.put("RPC_" + name, factoryBuilder);
            if (this.factoryBuilderMap.get("RPC_DEFAULT") == null) {
                this.factoryBuilderMap.put("RPC_DEFAULT", factoryBuilder);
            }
        }
        if (factoryBuilder instanceof MonitoringFactoryBuilder) {
            this.factoryBuilderMap.put("M_" + name, factoryBuilder);
            if (this.factoryBuilderMap.get("M_DEFAULT") == null) {
                this.factoryBuilderMap.put("M_DEFAULT", factoryBuilder);
            }
        }
        if (factoryBuilder instanceof SwitchFactoryBuilder) {
            this.factoryBuilderMap.put("S_" + name, factoryBuilder);
            if (this.factoryBuilderMap.get("S_DEFAULT") == null) {
                this.factoryBuilderMap.put("S_DEFAULT", factoryBuilder);
            }
        }
    }

    @Override
    public CacheClient getCacheClient(String group) {
        return this.getCacheClient("C_DEFAULT", group);
    }

    @Override
    public CacheClient getCacheClient(String name, String group) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<CacheFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) {
                throw new IllegalArgumentException("没有找到缓存实例实现");
            }
            CacheFactory cacheFactory = factoryBuilder.getFactory();

            return cacheFactory.build(group);
        }
        return null;
    }

    @Override
    public MQProducer getMQProducer(String group) {
        return this.getMQProducer("MQ_DEFAULT", group);
    }

    @Override
    public MQProducer getMQProducer(String name, String group) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<MQFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) throw new IllegalArgumentException("没有找到MQ实例实现");
            MQFactory mqFactory = factoryBuilder.getFactory();

            return mqFactory.buildProducer(group);
        }
        return null;
    }

    @Override
    public void registerMQConsumer(MQConsumer consumer) {
        this.registerMQConsumer("MQ_DEFAULT", consumer);
    }

    @Override
    public void registerMQConsumer(String name, MQConsumer consumer) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<MQFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) throw new IllegalArgumentException("没有找到MQ实例实现");
            MQFactory mqFactory = factoryBuilder.getFactory();
            mqFactory.registerConsumer(consumer);
        }
    }

    @Override
    public SearchEngineClient getSearchEngineClient(String group) {
        return this.getSearchEngineClient("SE_DEFAULT", group);
    }

    @Override
    public SearchEngineClient getSearchEngineClient(String name, String group) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<SearchEngineFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) throw new IllegalArgumentException("没有找到搜索引擎实例实现");
            SearchEngineFactory searchEngineFactory = factoryBuilder.getFactory();
            return searchEngineFactory.build(group);
        }
        return null;
    }

    @Override
    public <T> T getRPCObject(Class<T> tClass) {
        return this.getRPCObject("RPC_DEFAULT", tClass);
    }

    @Override
    public <T> T getRPCObject(String name, Class<T> tClass) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<RPCFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) throw new IllegalArgumentException("没有找到RPC实例实现");
            RPCFactory rpcFactory = factoryBuilder.getFactory();
            return rpcFactory.getObject(tClass);
        }
        return null;
    }

    @Override
    public void registerRPCObject(Object o) {
        this.registerRPCObject("RPC_DEFAULT", o);
    }

    @Override
    public void registerRPCObject(String name, Object o) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<RPCFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) throw new IllegalArgumentException("没有找到RPC实例实现");
            RPCFactory rpcFactory = factoryBuilder.getFactory();
            rpcFactory.register(o);
        }
    }

    @Override
    public Monitoring getMonitoring(String group) {
        return this.getMonitoring("M_DEFAULT", group);
    }

    @Override
    public Monitoring getMonitoring(String name, String group) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<MonitoringFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) throw new IllegalArgumentException("没有找到监控实例实现");
            MonitoringFactory monitoringFactory = factoryBuilder.getFactory();
            monitoringFactory.build(group);
        }
        return null;
    }

    @Override
    public Switch getSwitch(String group) {
        return getSwitch("S_DEFAULT", group);
    }

    @Override
    public Switch getSwitch(String name, String group) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<SwitchFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) throw new IllegalArgumentException("没有找到配置中心实例实现");
            SwitchFactory switchFactory = factoryBuilder.getFactory();
            return switchFactory.build(group);
        }
        return null;
    }

    @Override
    public void notifyChangedSwitch(SwitchChangedListener listener) {
        this.notifyChangedSwitch("S_DEFAULT", listener);
    }

    @Override
    public void notifyChangedSwitch(String name, SwitchChangedListener listener) {
        this.initFactoryBuilder();
        if (this.factoryBuilderMap != null) {
            FactoryBuilder<SwitchFactory> factoryBuilder = this.factoryBuilderMap.get(name);
            if (factoryBuilder == null) throw new IllegalArgumentException("没有找到配置中心实例实现");
            SwitchFactory switchFactory = factoryBuilder.getFactory();
            switchFactory.registerNotify(listener);
        }
    }

    private class SessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            AssistUtils.notNull(sessionFactory, "必须先设置SessionFactory");
            Session session = sessionFactory.openSession();
            Object object = null;
            try {
                object = method.invoke(session, args);
            } catch (Throwable throwable) {
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
