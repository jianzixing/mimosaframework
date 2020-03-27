package org.mimosaframework.orm.sql.create;


import org.mimosaframework.orm.sql.AbsColumnBuilder;

public interface CreateColumnAssistBuilder<T>
        extends
        ColumnAssistBuilder<CreateColumnAssistBuilder<T>>,
        AbsColumnBuilder<ColumnTypeBuilder<CreateColumnAssistBuilder<T>>>,
        CreateTableTailBuilder {
}
