package org.mimosaframework.core.json.serializer;

public abstract class BeforeFilter implements SerializeFilter {

    private static final ThreadLocal<ModelSerializer> serializerLocal = new ThreadLocal<ModelSerializer>();
    private static final ThreadLocal<Character>      seperatorLocal  = new ThreadLocal<Character>();

    private final static Character                   COMMA           = Character.valueOf(',');

    final char writeBefore(ModelSerializer serializer, Object object, char seperator) {
        serializerLocal.set(serializer);
        seperatorLocal.set(seperator);
        writeBefore(object);
        serializerLocal.set(null);
        return seperatorLocal.get();
    }

    protected final void writeKeyValue(String key, Object value) {
        ModelSerializer serializer = serializerLocal.get();
        char seperator = seperatorLocal.get();
        serializer.writeKeyValue(seperator, key, value);
        if (seperator != ',') {
            seperatorLocal.set(COMMA);
        }
    }

    public abstract void writeBefore(Object object);
}
