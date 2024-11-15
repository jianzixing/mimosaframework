package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;

/**
 * 单条查询语句中包含的所有条件,条件列表：
 * static final String operationEqual = "=";
 * static final String operationIn = "in";
 * static final String operationLike = "like";
 * static final String operationNotEqual = "!=";
 * static final String operationGt = ">";
 * static final String operationGtEqual = ">=";
 * static final String operationLt = "<";
 * static final String operationLtEqual = "<=";
 *
 * @author yangankang
 */
public interface Filter<T> {
    T eq(Object key, Object value);

    <F> T eq(FieldFunction<F> key, Object value);

    T in(Object key, Iterable<?> values);

    <F> T in(FieldFunction<F> key, Iterable<?> values);

    T in(Object key, Object... values);

    <F> T in(FieldFunction<F> key, Object... values);

    T nin(Object key, Iterable<?> values);

    <F> T nin(FieldFunction<F> key, Iterable<?> values);

    T nin(Object key, Object... values);

    <F> T nin(FieldFunction<F> key, Object... values);

    T like(Object key, Object value);

    <F> T like(FieldFunction<F> key, Object value);

    T ne(Object key, Object value);

    <F> T ne(FieldFunction<F> key, Object value);

    T gt(Object key, Object value);

    <F> T gt(FieldFunction<F> key, Object value);

    T gte(Object key, Object value);

    <F> T gte(FieldFunction<F> key, Object value);

    T lt(Object key, Object value);

    <F> T lt(FieldFunction<F> key, Object value);

    T lte(Object key, Object value);

    <F> T lte(FieldFunction<F> key, Object value);

    T between(Object key, Object start, Object end);

    <F> T between(FieldFunction<F> key, Object start, Object end);

    T isNull(Object key);

    <F> T isNull(FieldFunction<F> key);

    T isNotNull(Object key);

    <F> T isNotNull(FieldFunction<F> key);

    T when(boolean condition, Condition<T> then);

    T when(boolean condition, Condition<T> then, Condition<T> other);
}
