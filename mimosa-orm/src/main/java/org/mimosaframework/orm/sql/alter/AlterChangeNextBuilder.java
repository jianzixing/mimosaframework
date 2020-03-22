package org.mimosaframework.orm.sql.alter;


import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

public interface AlterChangeNextBuilder<T>
        extends
        AlterOldColumnBuilder<AlterNewColumnBuilder<ColumnTypeBuilder<AlterColumnAssistBuilder<T>>>> {
}
