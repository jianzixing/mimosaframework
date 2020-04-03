package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.AbsWhereValueBuilder;
import org.mimosaframework.orm.sql.OperatorEqualBuilder;

/**
 * for update set SetLinkBuilder
 *
 * @param <T>
 */
public interface SetLinkBuilder<T>
        extends
        UpdateSetColumnBuilder<OperatorEqualBuilder<AbsWhereValueBuilder<T>>> {
}
