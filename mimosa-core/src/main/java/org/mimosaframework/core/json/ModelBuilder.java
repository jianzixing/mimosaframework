package org.mimosaframework.core.json;

public class ModelBuilder {
    private Object object;
    private ModelBuilder parent;

    public ModelBuilder() {
        object = new ModelObject();
    }

    public ModelBuilder(ModelBuilder parent) {
        this.parent = parent;
    }

    public static ModelBuilder create() {
        return new ModelBuilder();
    }

    public ModelBuilder put(Object key, Object value) {
        if (object instanceof ModelObject) {
            ((ModelObject) object).put(key, value);
        }
        return this;
    }

    public ModelBuilder remove(Object key) {
        if (object instanceof ModelObject) {
            ((ModelObject) object).remove(key);
        }
        return this;
    }

    public ModelBuilder startModel() {
        if (object instanceof ModelArray) {
            return this.startModel(null);
        } else {
            throw new IllegalArgumentException("当前是Object对象");
        }
    }

    public ModelBuilder startModel(Object key) {
        ModelBuilder builder = new ModelBuilder(this);
        builder.object = new ModelObject();
        if (object instanceof ModelObject) {
            ((ModelObject) object).put(key, builder.object);
        }
        if (object instanceof ModelArray) {
            ((ModelArray) object).add(builder.object);
        }
        return builder;
    }

    public ModelBuilder startArray(Object key) {
        if (object instanceof ModelObject) {
            ModelBuilder builder = new ModelBuilder(this);
            builder.object = new ModelArray();
            ((ModelObject) object).put(key, builder.object);
            return builder;
        }
        return this;
    }

    public ModelBuilder endParent() {
        return this.parent;
    }

    public ModelObject toModelObject() {
        if (object instanceof ModelObject) {
            return (ModelObject) this.object;
        }
        return null;
    }

    public ModelArray toModelArray() {
        if (object instanceof ModelArray) {
            return (ModelArray) this.object;
        }
        return null;
    }

    public ModelObject toRootObject() {
        ModelBuilder mb = this;
        while (true) {
            if (mb.parent != null) {
                mb = mb.parent;
            } else {
                break;
            }
        }

        if (mb != null && mb.object instanceof ModelObject) {
            return (ModelObject) mb.object;
        }
        return null;
    }
}
