package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsTableAliasBuilder;

public interface SelectStartBuilder
        extends
        SelectFieldBuilder<ReplaceSelectFieldBuilder<AbsTableAliasBuilder<
                SelectTableAliasBuilder<SelectJoinsBuilder>>>> {
}
