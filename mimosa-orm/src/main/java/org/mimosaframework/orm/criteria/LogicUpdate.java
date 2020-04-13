package org.mimosaframework.orm.criteria;

public interface LogicUpdate extends Update {
    Update<LogicUpdate> and();

    Update<LogicUpdate> or();
}
