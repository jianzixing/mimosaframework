package org.mimosaframework.core.utils;

/**
 * 就是简单的KV单数据结构
 *
 * @param <T>
 * @param <S>
 */
public class EntryHolder<T, S> {
    private T key;
    private S value;

    public EntryHolder(T key, S value) {
        this.key = key;
        this.value = value;
    }

    public EntryHolder() {
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public S getValue() {
        return value;
    }

    public void setValue(S value) {
        this.value = value;
    }
}
