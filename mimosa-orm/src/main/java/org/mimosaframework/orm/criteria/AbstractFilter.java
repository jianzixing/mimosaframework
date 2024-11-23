package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;

public abstract class AbstractFilter<T> implements Filter<T> {
    public <F> T eq(FieldFunction<F> key, Object value) {
        return this.eq((Object) key, value);
    }

    @Override
    public <F> T in(FieldFunction<F> key, Iterable<?> values) {
        return this.in((Object) key, values);
    }

    @Override
    public <F> T in(FieldFunction<F> key, Object... values) {
        return this.in((Object) key, values);
    }

    @Override
    public <F> T nin(FieldFunction<F> key, Iterable<?> values) {
        return this.nin((Object) key, values);
    }

    @Override
    public <F> T nin(FieldFunction<F> key, Object... values) {
        return this.nin((Object) key, values);
    }

    @Override
    public <F> T like(FieldFunction<F> key, Object value) {
        return this.like((Object) key, value);
    }

    @Override
    public <F> T ne(FieldFunction<F> key, Object value) {
        return this.ne((Object) key, value);
    }

    @Override
    public <F> T gt(FieldFunction<F> key, Object value) {
        return this.gt((Object) key, value);
    }

    @Override
    public <F> T gte(FieldFunction<F> key, Object value) {
        return this.gte((Object) key, value);
    }

    @Override
    public <F> T lt(FieldFunction<F> key, Object value) {
        return this.lt((Object) key, value);
    }

    @Override
    public <F> T lte(FieldFunction<F> key, Object value) {
        return this.lte((Object) key, value);
    }

    @Override
    public <F> T between(FieldFunction<F> key, Object start, Object end) {
        return this.between((Object) key, start, end);
    }

    @Override
    public <F> T isNull(FieldFunction<F> key) {
        return this.isNull((Object) key);
    }

    @Override
    public <F> T isNotNull(FieldFunction<F> key) {
        return this.isNotNull((Object) key);
    }

    @Override
    public T when(boolean condition, Condition<T> then, Condition<T> other) {
        if (condition) {
            then.criteria(this);
        } else {
            other.criteria(this);
        }
        return (T) this;
    }

    @Override
    public T when(boolean condition, Condition<T> then) {
        if (condition) {
            then.criteria(this);
        }
        return (T) this;
    }
}
