package org.mimosaframework.orm.sql.update;

import java.io.Serializable;

public interface UpdateSetColumnBuilder<T> {
    T column(Serializable field);
}
