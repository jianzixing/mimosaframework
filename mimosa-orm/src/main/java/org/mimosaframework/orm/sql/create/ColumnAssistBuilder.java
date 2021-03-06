package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.*;

/**
 * A -> NOT NULL
 * B -> NULL
 * C -> DEFAULT
 * D -> AUTO_INCREMENT
 * E -> UNIQUE
 * F -> PRIMARY KEY
 * G -> COMMENT
 * H -> COLLATE
 */
public interface ColumnAssistBuilder<T>
        extends
        NotNullBuilder<T>,
        NullBuilder<T>,
        DefaultBuilder<T>,
        AutoIncrementBuilder<T>,
        PrimaryKeyBuilder<T>,
        CommentBuilder<T>,
        CollateBuilder<T> {
}
