package org.mimosaframework.orm.criteria;

public interface LogicQuery extends Query, LogicFilter<Query, LogicQuery> {
    Query and();

    Query or();
}
