package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;

public interface AlterRenameAnyBuilder<T>
        extends
        ColumnBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder<T>>>>,
        IndexBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder<T>>>>,
        AbsNameBuilder<T> {
}
