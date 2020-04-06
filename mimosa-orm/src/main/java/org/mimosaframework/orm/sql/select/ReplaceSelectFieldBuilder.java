package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AsBuilder;
import org.mimosaframework.orm.sql.FromBuilder;

public interface ReplaceSelectFieldBuilder<T>
        extends
        SelectFieldBuilder<SelectFieldFunBuilder<T>>,
        AsBuilder<ReplaceSelectFieldBuilder<T>>,
        FromBuilder<T> {
}
