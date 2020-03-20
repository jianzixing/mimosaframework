package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.ToBuilder;
import org.mimosaframework.orm.sql.AbsNameBuilder;
import org.mimosaframework.orm.sql.ColumnBuilder;
import org.mimosaframework.orm.sql.IndexBuilder;

public interface AlterRenameAnyBuilder
        extends
        ColumnBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder>>>,
        IndexBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder>>>,
        AbsNameBuilder {
}
