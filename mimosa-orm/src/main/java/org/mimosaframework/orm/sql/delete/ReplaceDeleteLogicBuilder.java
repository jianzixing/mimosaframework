package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.LogicBuilder;
import org.mimosaframework.orm.sql.OrderByBuilder;
import org.mimosaframework.orm.sql.OrderByNextBuilder;

public interface ReplaceDeleteLogicBuilder<T>
        extends
        LogicBuilder<T>,
        OrderByBuilder<OrderByNextBuilder> {
}
