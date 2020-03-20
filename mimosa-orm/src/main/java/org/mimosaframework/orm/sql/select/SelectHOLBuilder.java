package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.HavingBuilder;
import org.mimosaframework.orm.sql.LimitBuilder;
import org.mimosaframework.orm.sql.OrderByBuilder;
import org.mimosaframework.orm.sql.SortBuilder;

public interface SelectHOLBuilder
        extends
        HavingBuilder<HavingOperatorFunctionBuilder>,
        OrderByBuilder<SortBuilder<LimitBuilder>>,
        LimitBuilder {

}
