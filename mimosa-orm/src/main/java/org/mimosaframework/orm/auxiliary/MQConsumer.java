package org.mimosaframework.orm.auxiliary;

import java.util.List;

public interface MQConsumer {
    void onMessage(List<Message> messages) throws Exception;
}
