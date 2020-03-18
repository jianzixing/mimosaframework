package org.mimosaframework.orm.sql.test.insert;

import org.mimosaframework.orm.sql.test.ValuesBuilder;

import java.io.Serializable;

public interface InsertFieldBuilder<T>
        extends
        ValuesBuilder<T> {
    ValuesBuilder<T> wrapper(Serializable... fields);
}
