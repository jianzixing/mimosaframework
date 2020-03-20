package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.*;

public interface SelectGHOLBuilder
        extends
        GroupByBuilder<SelectHOLBuilder>,
        HavingBuilder<FieldFunBuilder<SelectOLBuilder>>,
        OrderByBuilder<SortBuilder<LimitBuilder>>,
        LimitBuilder {

}
