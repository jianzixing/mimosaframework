package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.SplitBuilder;

public interface AlterTableAnyBuilder
        extends
        AlterAddBuilder<AlterAddAnyBuilder<SplitBuilder<AlterTableAnyBuilder>>>,
        AlterChangeBuilder<AlterChangeNextBuilder<SplitBuilder<AlterTableAnyBuilder>>>,
        AlterModifyBuilder<AlterModifyNextBuilder<SplitBuilder<AlterTableAnyBuilder>>>,
        AlterDropBuilder<AlterDropAnyBuilder<SplitBuilder<AlterTableAnyBuilder>>>,
        AlterRenameBuilder<AlterRenameAnyBuilder<SplitBuilder<AlterTableAnyBuilder>>> {
}
