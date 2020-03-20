package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.LimitBuilder;
import org.mimosaframework.orm.sql.LogicBuilder;
import org.mimosaframework.orm.sql.OrderByBuilder;

public interface ReplaceDeleteLogicBuilder<T>
        extends
        LogicBuilder<T>,
        OrderByBuilder<LimitBuilder<Void>> {
}
