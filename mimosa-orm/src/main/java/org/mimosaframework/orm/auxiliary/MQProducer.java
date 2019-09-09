package org.mimosaframework.orm.auxiliary;

import java.util.List;
import java.util.Map;

public interface MQProducer extends AuxiliaryClient {
    void send(String message);

    void send(List<String> messages);
}
