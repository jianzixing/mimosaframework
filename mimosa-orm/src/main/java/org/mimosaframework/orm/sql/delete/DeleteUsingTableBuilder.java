package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AbsTableAliasBuilder;
import org.mimosaframework.orm.sql.CommonWhereBuilder;
import org.mimosaframework.orm.sql.UsingBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface DeleteUsingTableBuilder
        extends
        WhereBuilder<CommonWhereBuilder<DeleteWhereNextBuilder>>,
        AbsTableAliasBuilder<DeleteUsingTableBuilder> {
}
