package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsWhereColumnBuilder;
import org.mimosaframework.orm.sql.GroupByBuilder;

public interface SelectGroupByBuilder
        extends
        GroupByBuilder<AbsWhereColumnBuilder<SelectGroupByNextBuilder>> {
}
