package org.mimosaframework.orm.sql.test.alter;

public interface AlterTableAnyBuilder
        extends
        AlterAddBuilder<AlterAddAnyBuilder>,
        AlterChangeBuilder,
        AlterModifyBuilder,
        AlterDropBuilder,
        AlterRenameBuilder {
}
