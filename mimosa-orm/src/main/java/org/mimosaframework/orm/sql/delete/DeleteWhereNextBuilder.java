package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;

public interface DeleteWhereNextBuilder
        extends
        CommonWhereNextBuilder<DeleteWhereNextBuilder>
        // 不支持删除指定行数
        // LimitCountBuilder<UnifyBuilder>
{

}
