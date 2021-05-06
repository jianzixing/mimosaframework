package org.mimosaframework.orm.criteria;

import java.io.Serializable;

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
    T eq(Serializable key, Object value);

    T in(Serializable key, Iterable values);

    T in(Serializable key, Object... values);

    T nin(Serializable key, Iterable values);

    T nin(Serializable key, Object... values);

    T like(Serializable key, Object value);

    T ne(Serializable key, Object value);

    T gt(Serializable key, Object value);

    T gte(Serializable key, Object value);

    T lt(Serializable key, Object value);

    T lte(Serializable key, Object value);

    T between(Serializable key, Object start, Object end);

    T isNull(Serializable key);

    T isNotNull(Serializable key);
}
