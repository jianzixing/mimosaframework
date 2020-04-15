package org.mimosaframework.orm.criteria;

public interface LogicQuery extends Query<LogicQuery> {
    Query<LogicQuery> and();

    Query<LogicQuery> or();
}
