package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AboutChildBuilder;
import org.mimosaframework.orm.sql.LogicBuilder;

public interface ReplaceSelectLogicBuilder
        extends
        AboutChildBuilder,
        SelectGHOLBuilder,
        LogicBuilder<SelectWhereBuilder> {
}
