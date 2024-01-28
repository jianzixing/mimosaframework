package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.stamp.StampAction;

/**
 * 统一接口定义
 */
public interface UnifyBuilder {
    StampAction compile();
}
