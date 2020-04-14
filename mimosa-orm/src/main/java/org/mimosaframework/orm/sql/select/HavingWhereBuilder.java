package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsWhereValueBuilder;
import org.mimosaframework.orm.sql.FieldFunBuilder;
import org.mimosaframework.orm.sql.OperatorBuilder;

public interface HavingWhereBuilder
        extends FieldFunBuilder<OperatorBuilder<AbsWhereValueBuilder<HavingWhereNextBuilder>, HavingWhereNextBuilder>> {
}
