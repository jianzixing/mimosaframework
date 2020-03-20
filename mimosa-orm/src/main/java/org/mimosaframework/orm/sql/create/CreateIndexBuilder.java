package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.*;

public interface CreateIndexBuilder
        extends
        UniqueBuilder<IndexBuilder<AbsNameBuilder<OnBuilder<AbsTableBuilder<CreateIndexColumnsBuilder>>>>>,
        FullTextBuilder<IndexBuilder<AbsNameBuilder<OnBuilder<AbsTableBuilder<CreateIndexColumnsBuilder>>>>>,
        IndexBuilder<AbsNameBuilder<OnBuilder<AbsTableBuilder<CreateIndexColumnsBuilder>>>> {
}
