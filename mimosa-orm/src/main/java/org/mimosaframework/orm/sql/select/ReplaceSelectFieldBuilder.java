package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.FromBuilder;

public interface ReplaceSelectFieldBuilder<T>
        extends
        SelectFieldBuilder<SelectFieldFunBuilder<T>>,
        FromBuilder<T> {
}
