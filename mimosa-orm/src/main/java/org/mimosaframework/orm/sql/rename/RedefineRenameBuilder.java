package org.mimosaframework.orm.sql.rename;

import org.mimosaframework.orm.sql.*;

public interface RedefineRenameBuilder
        extends
        UnifyBuilder,
        RenameBuilder,
        ColumnBuilder,
        RenameOldColumnBuilder,
        ToBuilder,
        RenameNewColumnBuilder,
        OnBuilder,
        RenameTableNameBuilder,
        IndexBuilder,
        AbsNameBuilder {

}
