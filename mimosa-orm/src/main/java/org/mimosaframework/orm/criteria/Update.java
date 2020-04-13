package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Update extends Filter<Update> {
    Update setTableClass(Class c);

    Update linked(LogicLinked linked);

    Update and();

    Update or();

    Update set(Object key, Object value);

    Update addSelf(Object key);

    Update subSelf(Object key);

    Update addSelf(Object key, Integer step);

    Update subSelf(Object key, Integer step);

    Update addSelf(Object key, String step);

    Update subSelf(Object key, String step);

    Query covert2query();
}
