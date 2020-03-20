package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsWhereValueBuilder;
import org.mimosaframework.orm.sql.FieldFunBuilder;
import org.mimosaframework.orm.sql.OperatorBuilder;

public interface HavingOperatorFunctionBuilder
        extends FieldFunBuilder<OperatorBuilder<AbsWhereValueBuilder<SelectOLBuilder>>> {
}
