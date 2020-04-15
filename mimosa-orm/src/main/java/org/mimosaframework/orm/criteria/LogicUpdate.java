package org.mimosaframework.orm.criteria;

public interface LogicUpdate extends Update<LogicUpdate> {
    Update<LogicUpdate> and();

    Update<LogicUpdate> or();
}
