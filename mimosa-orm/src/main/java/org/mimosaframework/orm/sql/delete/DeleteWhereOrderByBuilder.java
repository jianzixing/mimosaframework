package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.OperatorLinkBuilder;
import org.mimosaframework.orm.sql.WrapperBuilder;

public interface DeleteWhereOrderByBuilder
        extends
        WrapperBuilder<ReplaceDeleteLogicBuilder<DeleteWhereOrderByBuilder>>,
        OperatorLinkBuilder<ReplaceDeleteLogicBuilder<DeleteWhereOrderByBuilder>> {
}
