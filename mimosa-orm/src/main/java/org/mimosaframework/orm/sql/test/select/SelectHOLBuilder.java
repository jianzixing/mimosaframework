package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.*;

public interface SelectHOLBuilder
        extends
        HavingBuilder<HavingOperatorFunctionBuilder>,
        OrderByBuilder<SortBuilder<LimitBuilder>>,
        LimitBuilder {

}
