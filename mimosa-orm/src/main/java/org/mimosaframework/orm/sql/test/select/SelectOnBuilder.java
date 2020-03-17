package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.LogicBuilder;
import org.mimosaframework.orm.sql.test.WhereItemBuilder;
import org.mimosaframework.orm.sql.test.WrapperBuilder;

public interface SelectOnBuilder
        extends
        WrapperBuilder<LogicBuilder<SelectOnBuilder>>,
        WhereItemBuilder<LogicBuilder<SelectOnBuilder>> {
}
