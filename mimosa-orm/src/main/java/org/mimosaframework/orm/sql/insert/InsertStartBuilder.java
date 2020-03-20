package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.IntoBuilder;
import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface InsertStartBuilder
        extends IntoBuilder<AbsTableBuilder<InsertFieldBuilder<ReplaceInsertValuesBuilder>>> {
}
