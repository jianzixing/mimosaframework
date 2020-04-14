package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.AbsColumnsBuilder;
import org.mimosaframework.orm.sql.InsertBuilder;
import org.mimosaframework.orm.sql.IntoBuilder;
import org.mimosaframework.orm.sql.ValuesBuilder;

public interface RedefineInsertBuilder
        extends
        InsertBuilder,
        IntoBuilder,
        InsertTableNameBuilder,
        AbsColumnsBuilder,
        ValuesBuilder,
        InsertValuesBuilder {
}
