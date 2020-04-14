package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsNameBuilder;
import org.mimosaframework.orm.sql.ColumnBuilder;
import org.mimosaframework.orm.sql.IndexBuilder;
import org.mimosaframework.orm.sql.ToBuilder;

public interface AlterRenameAnyBuilder<T>
        extends
        ColumnBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder<T>>>>,
        IndexBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder<T>>>>,
        AbsNameBuilder<T> {
}
