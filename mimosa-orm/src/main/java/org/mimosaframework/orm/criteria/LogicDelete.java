package org.mimosaframework.orm.criteria;

public interface LogicDelete extends Delete {
    Delete and();

    Delete or();
}
