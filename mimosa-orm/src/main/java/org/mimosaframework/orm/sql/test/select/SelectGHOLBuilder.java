package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.*;

public interface SelectGHOLBuilder
        extends
        GroupByBuilder<SelectHOLBuilder>,
        HavingBuilder<HavingOperatorFunctionBuilder>,
        OrderByBuilder<SortBuilder<LimitBuilder>>,
        LimitBuilder {

}
