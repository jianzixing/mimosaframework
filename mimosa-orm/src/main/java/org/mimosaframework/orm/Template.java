package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

public interface Template {
    <T> Query<T> query(Class<T> clazz);

    Delete delete(Class clazz);

    Update update(Class clazz);
}
