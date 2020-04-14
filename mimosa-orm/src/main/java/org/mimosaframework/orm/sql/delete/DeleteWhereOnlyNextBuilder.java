package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.CommonWhereNextBuilder;

/**
 * delete 表 from 表
 * 以上的语句不支持order by 和 limit
 */
public interface DeleteWhereOnlyNextBuilder
        extends
        CommonWhereNextBuilder<DeleteWhereOnlyNextBuilder> {

}
