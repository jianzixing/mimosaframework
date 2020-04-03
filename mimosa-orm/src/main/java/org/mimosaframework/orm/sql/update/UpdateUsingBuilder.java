package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.SetBuilder;
import org.mimosaframework.orm.sql.UsingBuilder;

public interface UpdateUsingBuilder<T>
        extends
        UsingBuilder<UpdateTableAliasBuilder<T>>,
        SetBuilder<T> {
}
