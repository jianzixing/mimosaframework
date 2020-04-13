package org.mimosaframework.orm.criteria;

public interface LogicDelete extends Delete {
    Delete<LogicDelete> and();

    Delete<LogicDelete> or();
}
