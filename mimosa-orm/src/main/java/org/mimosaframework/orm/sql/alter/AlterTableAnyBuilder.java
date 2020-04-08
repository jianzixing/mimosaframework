package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.UnifyBuilder;

public interface AlterTableAnyBuilder
        extends
        AlterAddBuilder<AlterAddAnyBuilder<UnifyBuilder>>,
        AlterChangeBuilder<AlterChangeNextBuilder<UnifyBuilder>>,
        AlterModifyBuilder<AlterModifyNextBuilder<UnifyBuilder>>,
        AlterDropBuilder<AlterDropAnyBuilder<UnifyBuilder>>,
        AlterRenameBuilder<AlterRenameAnyBuilder<UnifyBuilder>> {
}
