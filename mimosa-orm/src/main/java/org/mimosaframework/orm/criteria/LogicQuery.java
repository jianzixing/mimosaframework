package org.mimosaframework.orm.criteria;

public interface LogicQuery extends Query {
    Query<LogicQuery> and();

    Query<LogicQuery> or();
}
