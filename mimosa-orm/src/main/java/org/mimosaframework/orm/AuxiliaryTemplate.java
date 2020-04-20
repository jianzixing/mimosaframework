package org.mimosaframework.orm;

import org.mimosaframework.orm.auxiliary.*;

public interface AuxiliaryTemplate {

    /**
     * 获得缓存客户端，这个cache不限定特定服务
     *
     * @return
     */
    CacheClient getCacheClient(String group);

    /**
     * 通过缓存配置名称获得缓存客户端
     *
     * @param name
     * @param group
     * @return
     */
    CacheClient getCacheClient(String name, String group);

    /**
     * 获得MQ消息队列实现
     *
     * @return
     */
    MQProducer getMQProducer(String group);

    /**
     * 通过配置名称获取MQ消息队列实现
     *
     * @param name
     * @return
     */
    MQProducer getMQProducer(String name, String group);

    /**
     * 注册MQ消息队列消费者
     *
     * @param consumer
     */
    void registerMQConsumer(MQConsumer consumer);

    void registerMQConsumer(String name, MQConsumer consumer);

    /**
     * 获取搜索引擎实现
     *
     * @return
     */
    SearchEngineClient getSearchEngineClient(String group);

    /**
     * 通过名称获取搜索引擎实现
     *
     * @param name
     * @return
     */
    SearchEngineClient getSearchEngineClient(String name, String group);

    /**
     * 获取远程接口调用实现
     *
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T getRPCObject(Class<T> tClass);

    /**
     * 通过别名获取远程接口调用实现，比如dubbo
     *
     * @param tClass
     * @param name
     * @param <T>
     * @return
     */
    <T> T getRPCObject(String name, Class<T> tClass);

    /**
     * 启动远程调用接口服务，这里不做服务初始化而是动态加入接口
     *
     * @param o
     */
    void registerRPCObject(String name, Object o);

    void registerRPCObject(Object o);

    /**
     * 获取监控实现
     * 监控实现包括当前vm信息，系统硬件信息，已经程序的不同等级日志
     *
     * @return
     */
    Monitoring getMonitoring(String group);

    /**
     * 通过别名获取监控实现
     * 监控实现包括当前vm信息，系统硬件信息，已经程序的不同等级日志
     *
     * @return
     */
    Monitoring getMonitoring(String name, String group);

    /**
     * 获取配置中心实现
     *
     * @return
     */
    Switch getSwitch(String group);

    /**
     * 获取配置中心实现
     *
     * @return
     */
    Switch getSwitch(String name, String group);

    /**
     * 如果配置中心配置有改变则通知
     *
     * @param listener
     */
    void notifyChangedSwitch(SwitchChangedListener listener);

    void notifyChangedSwitch(String name, SwitchChangedListener listener);
}
