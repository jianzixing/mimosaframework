package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.UnifyBuilder;

public interface AlterTableAnyBuilder
        extends
        AlterAddBuilder<AlterAddAnyBuilder<UnifyBuilder>>,
        AlterModifyBuilder<AlterModifyNextBuilder<UnifyBuilder>>,
        AlterDropBuilder<AlterDropAnyBuilder<UnifyBuilder>> {
}
