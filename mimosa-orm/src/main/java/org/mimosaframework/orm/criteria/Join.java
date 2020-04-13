package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Join extends Filter<Join> {
    Join childJoin(Join join);

    Join on(Object mainField, Object self);

    Join jeq(Object mainField, Object self);

    Join jne(Object mainField, Object self);

    Join jgt(Object mainField, Object self);

    Join jge(Object mainField, Object self);

    Join jlt(Object mainField, Object self);

    Join jle(Object mainField, Object self);

    Join aliasName(Object s);

    Join single();

    Join multiple();

    Class getTableClass();

    Class getMainClass();
}
