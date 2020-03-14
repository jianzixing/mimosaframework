package org.mimosaframework.orm.sql.test;

public interface WhereWrapperStartBuilder extends WhereItemBuilder<WhereLogicOnlyBuilder> {

    WhereLogicOnlyBuilder wrapper(WhereItemBuilder itemBuilder);

    WhereLogicOnlyBuilder wrapper(WhereLogicOnlyBuilder logicOnlyBuilder);
}
