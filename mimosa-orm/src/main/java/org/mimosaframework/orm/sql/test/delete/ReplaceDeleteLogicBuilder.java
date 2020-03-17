package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.LimitBuilder;
import org.mimosaframework.orm.sql.test.LogicBuilder;
import org.mimosaframework.orm.sql.test.OrderByBuilder;

public interface ReplaceDeleteLogicBuilder<T> extends LogicBuilder<T>, OrderByBuilder<LimitBuilder<Void>> {
}
