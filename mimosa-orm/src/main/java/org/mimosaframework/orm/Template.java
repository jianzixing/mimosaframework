package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

public interface Template {
    Query query(Class clazz);

    Delete delete(Class clazz);

    Update update(Class clazz);
}
