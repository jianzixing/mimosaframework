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
public interface CreateColumnAssistBuilder
        extends
        AboutTableColumn,

        NotNullBuilder<CreateColumnAssistBuilder>,
        NullBuilder<CreateColumnAssistBuilder>,
        DefaultBuilder<CreateColumnAssistBuilder>,
        AutoIncrementBuilder<CreateColumnAssistBuilder>,
        UniqueBuilder<CreateColumnAssistBuilder>,
        PrimaryKeyBuilder<CreateColumnAssistBuilder>,
        KeyBuilder<CreateColumnAssistBuilder>,
        CommentBuilder<CreateColumnAssistBuilder>,
        CollateBuilder<CreateColumnAssistBuilder> {
}
