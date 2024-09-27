package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;

/**
 * @author yangankang
 */
public interface Join extends Filter<Join> {
    int getJoinType();

    Join subjoin(Join join);

    Join on(Object self, Object mainField);

    <F> Join on(FieldFunction<F> self, FieldFunction<F> mainField);

    Join oneq(Object self, Object mainField);

    <F> Join oneq(FieldFunction<F> self, FieldFunction<F> mainField);

    Join onne(Object self, Object mainField);

    <F> Join onne(FieldFunction<F> self, FieldFunction<F> mainField);

    Join ongt(Object self, Object mainField);

    <F> Join ongt(FieldFunction<F> self, FieldFunction<F> mainField);

    Join onge(Object self, Object mainField);

    <F> Join onge(FieldFunction<F> self, FieldFunction<F> mainField);

    Join onlt(Object self, Object mainField);

    <F> Join onlt(FieldFunction<F> self, FieldFunction<F> mainField);

    Join onle(Object self, Object mainField);

    <F> Join onle(FieldFunction<F> self, FieldFunction<F> mainField);

    Join aliasName(Object s);

    Join as(Object s);

    Join single();

    Join multiple();

    Join ignore();

    Join ignore(boolean is);

    Join orderBy(OrderBy order);

    Join orderBy(Object field, boolean isAsc);

    <F> Join orderBy(FieldFunction<F> field, boolean isAsc);

    Join orderBy(Object field, Sort sort);

    <F> Join orderBy(FieldFunction<F> field, Sort sort);

    Class getTableClass();

    Class getMainClass();
}
