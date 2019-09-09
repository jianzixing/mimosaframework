package org.mimosaframework.core.json.serializer;

import java.io.IOException;

public interface ContextObjectSerializer extends ObjectSerializer {
    void write(ModelSerializer serializer, //
               Object object, //
               BeanContext context) throws IOException;
}
