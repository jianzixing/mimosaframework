package org.mimosaframework.core.json;

import java.util.Map;

public class ModelObjectBuilder {
    private Class<?> tableContactsClass;
    private ModelObject object = new ModelObject();

    private Object key;
    private ModelObjectBuilder objectBuilder;
    private ModelArrayBuilder arrayBuilder;

    public ModelObjectBuilder(Class<?> tableContactsClass) {
        this.tableContactsClass = tableContactsClass;
        object.setObjectClass(tableContactsClass);
    }

    public ModelObjectBuilder(Object key, ModelObjectBuilder builder) {
        this.key = key;
        this.objectBuilder = builder;
    }

    public ModelObjectBuilder(ModelArrayBuilder builder) {
        this.arrayBuilder = builder;
    }

    public ModelObjectBuilder() {
    }

    public ModelObject build() {
        return object;
    }

    public ModelObjectBuilder put(Object key, Object value) {
        object.put(key, value);
        return this;
    }

    public ModelObjectBuilder putAll(Map<?, ?> data) {
        object.putAll(data);
        return this;
    }

    public ModelObjectBuilder remove(Object key) {
        object.remove(key);
        return this;
    }

    public ModelArrayBuilder enterArray(Object key) {
        return new ModelArrayBuilder(key, this);
    }

    public ModelObjectBuilder enterObject(Object key) {
        return new ModelObjectBuilder(key, this);
    }

    public ModelObjectBuilder leaveObject() {
        return this.objectBuilder.put(key, object);
    }

    public ModelArrayBuilder leaveArray() {
        return this.arrayBuilder.add(object);
    }
}
