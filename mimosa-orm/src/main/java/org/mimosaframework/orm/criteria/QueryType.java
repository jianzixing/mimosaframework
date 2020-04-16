package org.mimosaframework.orm.criteria;

/**
 * 指定查询方式
 * NORMAL   标准查询语句，使用一条SQL语句执行获取结果
 * LEFT     LEFT JOIN语句单独作为一条查询执行获取结果，并手动判断合并结果集
 * ANY      所有JOIN语句都作为单独一条查询语句执行获取结果，并手动判断合并结果集(效率非常低暂时不支持)
 */
public enum QueryType {
    NORMAL,
    LEFT
    // , ANY
}
