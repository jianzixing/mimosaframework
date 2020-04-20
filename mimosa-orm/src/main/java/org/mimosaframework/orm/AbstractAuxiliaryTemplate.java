package org.mimosaframework.orm;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.auxiliary.*;
import org.mimosaframework.orm.i18n.I18n;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractAuxiliaryTemplate implements AuxiliaryTemplate {
    private SessionFactory sessionFactory;
    private Map<String, FactoryBuilder> factoryBuilderMap;

    public AbstractAuxiliaryTemplate() {
    }

    public AbstractAuxiliaryTemplate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
            throw new IllegalArgumentException(I18n.print("create_factory_error"), e);
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
                throw new IllegalArgumentException(I18n.print("not_found_cache"));
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
            if (factoryBuilder == null) throw new IllegalArgumentException(I18n.print("not_found_mq"));
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
            if (factoryBuilder == null) throw new IllegalArgumentException(I18n.print("not_found_mq"));
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
            if (factoryBuilder == null) throw new IllegalArgumentException(I18n.print("not_found_search"));
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
            if (factoryBuilder == null) throw new IllegalArgumentException(I18n.print("not_found_rpc"));
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
            if (factoryBuilder == null) throw new IllegalArgumentException(I18n.print("not_found_rpc"));
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
            if (factoryBuilder == null) throw new IllegalArgumentException(I18n.print("not_found_monitor"));
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
            if (factoryBuilder == null) throw new IllegalArgumentException(I18n.print("not_found_center"));
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
            if (factoryBuilder == null) throw new IllegalArgumentException(I18n.print("not_found_center"));
            SwitchFactory switchFactory = factoryBuilder.getFactory();
            switchFactory.registerNotify(listener);
        }
    }
}
