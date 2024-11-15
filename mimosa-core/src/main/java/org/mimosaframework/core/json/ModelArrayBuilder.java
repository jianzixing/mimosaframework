package org.mimosaframework.core.json;

import java.util.Collection;

public class ModelArrayBuilder {
    private Object key;
    private ModelObjectBuilder objectBuilder;
    private ModelArrayBuilder arrayBuilder;
    private ModelArray object = new ModelArray();

    public ModelArrayBuilder(Object key, ModelObjectBuilder builder) {
        this.key = key;
        this.objectBuilder = builder;
    }

    public ModelArrayBuilder(ModelArrayBuilder builder) {
        this.arrayBuilder = builder;
    }

    public ModelArrayBuilder() {
    }

    public ModelArray build() {
        return object;
    }

    public ModelArrayBuilder add(Object value) {
        this.object.add(value);
        return this;
    }

    public ModelArrayBuilder add(int index, Object value) {
        this.object.set(index, value);
        return this;
    }

    public ModelArrayBuilder remove(Object value) {
        this.object.remove(value);
        return this;
    }

    public ModelArrayBuilder remove(int value) {
        this.object.remove(value);
        return this;
    }

    public ModelArrayBuilder removeAll(Collection<?> list) {
        this.object.removeAll(list);
        return this;
    }

    public ModelArrayBuilder clear() {
        this.object.clear();
        return this;
    }

    public ModelArrayBuilder addAll(Collection<?> list) {
        this.object.addAll(list);
        return this;
    }

    public ModelObjectBuilder leaveObject() {
        return objectBuilder.put(this.key, object);
    }

    public ModelArrayBuilder leaveArray() {
        return arrayBuilder.add(object);
    }
}
