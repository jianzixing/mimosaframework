package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.*;

public interface Template {
    Query<LogicQuery> buildQuery(Class clazz);

    Delete<LogicDelete> buildDelete(Class clazz);

    Update<LogicUpdate> buildUpdate(Class clazz);
}
