package org.mimosaframework.orm.criteria;

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

    T in(Object key, Iterable values);

    T in(Object key, Object... values);

    T nin(Object key, Iterable values);

    T nin(Object key, Object... values);

    T like(Object key, Object value);

    T ne(Object key, Object value);

    T gt(Object key, Object value);

    T gte(Object key, Object value);

    T lt(Object key, Object value);

    T lte(Object key, Object value);

    T between(Object key, Object start, Object end);

    T isNull(Object key);

    T isNotNull(Object key);
}
