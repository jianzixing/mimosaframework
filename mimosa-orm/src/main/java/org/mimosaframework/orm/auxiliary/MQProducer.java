package org.mimosaframework.orm.auxiliary;

import java.util.List;

public interface MQProducer extends AuxiliaryClient {
    void send(String message);

    void send(List<String> messages);
}
