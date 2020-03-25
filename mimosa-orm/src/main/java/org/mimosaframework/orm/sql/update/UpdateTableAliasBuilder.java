package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.AbsTableAliasBuilder;
import org.mimosaframework.orm.sql.SetBuilder;

public interface UpdateTableAliasBuilder
        extends
        AbsTableAliasBuilder<UpdateTableAliasBuilder>,
        SetBuilder<ReplaceUpdateSetBuilder> {
}
