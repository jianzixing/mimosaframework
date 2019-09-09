package org.mimosaframework.orm.auxiliary;

public class SearchSort {
    private Object key;
    private Type type;

    public static SearchSort create(Object key, Type type) {
        return new SearchSort(key, type);
    }

    public SearchSort(Object key, Type type) {
        this.key = key;
        this.type = type;
    }

    public Object getKey() {
        return key;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        ASC, DESC
    }
}
