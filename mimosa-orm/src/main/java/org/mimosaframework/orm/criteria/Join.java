package org.mimosaframework.orm.criteria;

import java.io.Serializable;

/**
 * @author yangankang
 */
public interface Join extends Filter<Join> {
    int getJoinType();

    Join subjoin(Join join);

    Join on(Object self, Object mainField);

    Join oneq(Object self, Object mainField);

    Join onne(Object self, Object mainField);

    Join ongt(Object self, Object mainField);

    Join onge(Object self, Object mainField);

    Join onlt(Object self, Object mainField);

    Join onle(Object self, Object mainField);

    Join aliasName(Object s);

    Join as(Serializable s);

    Join single();

    Join multiple();

    Join ignore();

    Join ignore(boolean is);

    Join orderBy(OrderBy order);

    Join orderBy(Serializable field, boolean isAsc);

    Class getTableClass();

    Class getMainClass();
}
