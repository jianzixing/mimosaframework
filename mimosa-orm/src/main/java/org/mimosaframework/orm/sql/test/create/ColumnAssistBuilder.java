package org.mimosaframework.orm.sql.test.create;

import org.mimosaframework.orm.sql.test.*;

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
        UniqueBuilder<T>,
        PrimaryKeyBuilder<T>,
        KeyBuilder<T>,
        CommentBuilder<T>,
        CollateBuilder<T> {
}
