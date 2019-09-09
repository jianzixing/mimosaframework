package org.mimosaframework.orm.criteria;

import java.util.List;

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
public interface Filter {

    Query query();

    Join join();

    Update update();

    Delete delete();

    Filter eq(Object key, Object value);

    Filter in(Object key, Iterable values);

    Filter in(Object key, Object... values);

    Filter nin(Object key, Iterable values);

    Filter nin(Object key, Object... values);

    Filter like(Object key, Object value);

    Filter ne(Object key, Object value);

    Filter gt(Object key, Object value);

    Filter gte(Object key, Object value);

    Filter lt(Object key, Object value);

    Filter lte(Object key, Object value);

    Filter between(Object key, Object start, Object end);

    Filter isNull(Object key);

    Filter isNotNull(Object key);
}
