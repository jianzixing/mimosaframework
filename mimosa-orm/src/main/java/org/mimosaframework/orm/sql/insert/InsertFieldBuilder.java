package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.AbsColumnsBuilder;
import org.mimosaframework.orm.sql.ValuesBuilder;

public interface InsertFieldBuilder<T>
        extends
        AbsColumnsBuilder<ValuesBuilder<T>> {
}
