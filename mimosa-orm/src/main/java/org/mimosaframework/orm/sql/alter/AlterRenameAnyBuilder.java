package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;

public interface AlterRenameAnyBuilder
        extends
        ColumnBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder<UnifyBuilder>>>>,
        IndexBuilder<AlterOldColumnBuilder<ToBuilder<AlterNewColumnBuilder<UnifyBuilder>>>>,
        AbsNameBuilder<UnifyBuilder> {
}
