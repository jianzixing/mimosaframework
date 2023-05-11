package org.mimosaframework.orm.sql.stamp;

public enum KeyWhereType {
    NORMAL,     // 标准的表达式 A=B 或者 count(A)>1 或者 A in (1,2,3) 类似
    KEY_AND,    // 关键字类型 用AND连接 比如 between 1 and 10
    FUN,        // 函数类型 比如 isNull
    WRAP,       // 子语句比如 (a > b)
    EXISTS      // 子查询语句
}
