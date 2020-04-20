package org.mimosaframework.orm.sql.rename;

import org.mimosaframework.orm.sql.*;

public interface RenameAnyBuilder
        extends
        ColumnBuilder<RenameOldColumnBuilder<ToBuilder<RenameNewColumnBuilder<OnBuilder<RenameTableNameBuilder<UnifyBuilder>>>>>>,
        IndexBuilder<RenameOldColumnBuilder<ToBuilder<RenameNewColumnBuilder<OnBuilder<RenameTableNameBuilder<UnifyBuilder>>>>>>,
        RenameTableNameBuilder<ToBuilder<AbsNameBuilder<UnifyBuilder>>> {
}
