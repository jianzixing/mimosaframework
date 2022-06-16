package org.mimosaframework.orm.criteria;

public interface LogicQuery extends Query<LogicQuery>, LogicFilter<Query<LogicQuery>, LogicQuery> {
    Query<LogicQuery> and();

    Query<LogicQuery> or();
}
