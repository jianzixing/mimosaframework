package org.mimosaframework.orm.criteria;

public interface LogicUpdate extends Update {
    Update and();

    Update or();
}
