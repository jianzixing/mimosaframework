package org.mimosaframework.core.json.serializer;


@Deprecated
public class ModelSerializerMap extends SerializeConfig {
    public final boolean put(Class<?> clazz, ObjectSerializer serializer) {
        return super.putInternal(clazz, serializer);
    }
}
