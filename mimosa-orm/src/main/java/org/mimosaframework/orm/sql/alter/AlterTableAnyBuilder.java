package org.mimosaframework.orm.sql.alter;

public interface AlterTableAnyBuilder
        extends
        AlterAddBuilder<AlterAddAnyBuilder>,
        AlterChangeBuilder<AlterChangeNextBuilder>,
        AlterModifyBuilder<AlterModifyNextBuilder>,
        AlterDropBuilder<AlterDropAnyBuilder>,
        AlterRenameBuilder<AlterRenameAnyBuilder> {
}
