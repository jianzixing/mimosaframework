package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Delete extends Filter<Delete> {
    Delete setTableClass(Class c);

    Delete linked(LogicLinked linked);

    Delete and();

    Delete or();

    Query covert2query();
}
