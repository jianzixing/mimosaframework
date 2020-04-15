package org.mimosaframework.orm.criteria;

public interface LogicDelete extends Delete<LogicDelete> {
    Delete<LogicDelete> and();

    Delete<LogicDelete> or();
}
