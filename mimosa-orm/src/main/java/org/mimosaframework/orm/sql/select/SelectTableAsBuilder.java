package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface SelectTableAsBuilder
        extends
        AbsTableBuilder<SelectTableAsBuilder>,
        ReplaceSelectWhereBuilder {
}
