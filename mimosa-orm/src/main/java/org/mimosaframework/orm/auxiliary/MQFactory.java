package org.mimosaframework.orm.auxiliary;

public interface MQFactory {
    MQProducer buildProducer(String group);

    boolean registerConsumer(MQConsumer consumer);
}
