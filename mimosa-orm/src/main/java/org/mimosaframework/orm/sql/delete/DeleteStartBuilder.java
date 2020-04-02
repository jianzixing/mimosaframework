package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;

public interface DeleteStartBuilder
        extends
        BeforeDeleteFromBuilder
                <FromBuilder
                        <AbsTableAliasBuilder
                                <DeleteAsTableBuilder<CommonWhereBuilder<DeleteWhereOnlyNextBuilder>>>>>,

        FromBuilder
                <AbsTableBuilder
                        <DeleteUsingBuilder>> {
}
