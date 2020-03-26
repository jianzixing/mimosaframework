package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AsBuilder;

public interface SelectFieldFunBuilder<T>
        extends
        AsBuilder<ReplaceSelectFieldBuilder<T>>,
        ReplaceSelectFieldBuilder<T> {
}
