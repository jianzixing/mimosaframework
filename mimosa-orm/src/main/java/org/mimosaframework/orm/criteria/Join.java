package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;

/**
 * @author yangankang
 */
public interface Join extends Filter<Join> {
    int getJoinType();

    Join subjoin(Join join);

    Join on(Object self, Object mainField);

    <L, R> Join on(FieldFunction<L> self, FieldFunction<R> mainField);

    Join oneq(Object self, Object mainField);

    <L, R> Join oneq(FieldFunction<L> self, FieldFunction<R> mainField);

    Join onne(Object self, Object mainField);

    <L, R> Join onne(FieldFunction<L> self, FieldFunction<R> mainField);

    Join ongt(Object self, Object mainField);

    <L, R> Join ongt(FieldFunction<L> self, FieldFunction<R> mainField);

    Join onge(Object self, Object mainField);

    <L, R> Join onge(FieldFunction<L> self, FieldFunction<R> mainField);

    Join onlt(Object self, Object mainField);

    <L, R> Join onlt(FieldFunction<L> self, FieldFunction<R> mainField);

    Join onle(Object self, Object mainField);

    <L, R> Join onle(FieldFunction<L> self, FieldFunction<R> mainField);

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
