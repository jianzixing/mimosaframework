package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.CommonWhereBuilder;
import org.mimosaframework.orm.sql.UsingBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface DeleteUsingBuilder
        extends
        WhereBuilder<CommonWhereBuilder<DeleteWhereNextBuilder>>,
        UsingBuilder<DeleteUsingTableBuilder> {
}
