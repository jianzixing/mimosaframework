package org.mimosaframework.orm.sql;

public interface OrderBySealCountBuilder
        extends
        UnifyBuilder,
        OrderByNextCountBuilder
        // 不支持删除指定行数
        // LimitCountBuilder<UnifyBuilder>
{
}
