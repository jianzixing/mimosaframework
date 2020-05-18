package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.*;

public interface Template {
    Query<LogicQuery> query(Class clazz);

    Delete<LogicDelete> delete(Class clazz);

    Update<LogicUpdate> update(Class clazz);
}
