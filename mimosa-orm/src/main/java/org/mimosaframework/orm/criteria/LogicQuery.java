package org.mimosaframework.orm.criteria;

public interface LogicQuery extends Query {
    Query and();

    Query or();
}
