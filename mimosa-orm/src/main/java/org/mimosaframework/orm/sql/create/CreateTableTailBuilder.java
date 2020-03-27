package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AbsExtraBuilder;
import org.mimosaframework.orm.sql.CharsetBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

public interface CreateTableTailBuilder
        extends
        CharsetBuilder<CreateCollateExtraBuilder<UnifyBuilder>>,
        AbsExtraBuilder<UnifyBuilder> {
}
