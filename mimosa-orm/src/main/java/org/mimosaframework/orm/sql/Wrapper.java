package org.mimosaframework.orm.sql;

public class Wrapper {
    public static CommonWhereBuilder<CommonWhereNextImplBuilder> build() {
        return SQLActionFactory.wrapperBuilder();
    }
}
