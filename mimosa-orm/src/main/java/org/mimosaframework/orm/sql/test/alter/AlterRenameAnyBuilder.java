package org.mimosaframework.orm.sql.test.alter;

import org.mimosaframework.orm.sql.test.AbsNameBuilder;
import org.mimosaframework.orm.sql.test.ColumnBuilder;
import org.mimosaframework.orm.sql.test.IndexBuilder;
import org.mimosaframework.orm.sql.test.ToBuilder;

public interface AlterRenameAnyBuilder
        extends
        ColumnBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder>>>,
        IndexBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder>>>,
        AbsNameBuilder {
}
