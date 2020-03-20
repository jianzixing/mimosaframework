package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.CommonOrderByBuilder;
import org.mimosaframework.orm.sql.LogicBuilder;

public interface ReplaceDeleteLogicBuilder<T>
        extends
        LogicBuilder<T>,
        CommonOrderByBuilder<Void> {
}
