package org.mimosaframework.orm.sql.test.alter;

public interface AlterTableAnyBuilder
        extends
        AlterAddBuilder<AlterAddAnyBuilder>,
        AlterChangeBuilder<AlterChangeNextBuilder>,
        AlterModifyBuilder<AlterModifyNextBuilder>,
        AlterDropBuilder<AlterDropAnyBuilder>,
        AlterRenameBuilder<AlterRenameAnyBuilder> {
}
