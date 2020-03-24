package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface SelectStartBuilder
        extends
        SelectFieldBuilder<ReplaceSelectFieldBuilder<AbsTableBuilder<SelectAsTableBuilder>>> {
}
