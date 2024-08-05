package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.*;

public interface Template {
    Query buildQuery(Class<?> clazz);

    Delete buildDelete(Class<?> clazz);

    Update buildUpdate(Class<?> clazz);
}
