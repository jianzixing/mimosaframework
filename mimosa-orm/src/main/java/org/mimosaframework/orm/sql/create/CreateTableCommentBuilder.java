package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.CharsetBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

public interface CreateTableCommentBuilder
        extends
        CharsetBuilder<CreateCollateExtraBuilder<UnifyBuilder>>,
        CreateCommentForBuilder<CharsetBuilder<CreateCollateExtraBuilder<UnifyBuilder>>> {
}
