package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.WhereItemBuilder;
import org.mimosaframework.orm.sql.test.WrapperBuilder;

public interface DeleteWhereBuilder
        extends
        WrapperBuilder<ReplaceDeleteLogicBuilder<DeleteWhereBuilder>>,
        WhereItemBuilder<ReplaceDeleteLogicBuilder<DeleteWhereBuilder>> {
}
