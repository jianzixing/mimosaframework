package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.IntoBuilder;

public interface InsertStartBuilder
        extends IntoBuilder<InsertTableNameBuilder<InsertFieldBuilder<ReplaceInsertValuesBuilder>>> {
}
