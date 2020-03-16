package org.mimosaframework.orm.sql.test;

public class GlobalBuilder {
    public static SelectFieldBuilder select() {
        return null;
    }

    public static DeleteTableBuilder delete() {
        return null;
    }

    public static WhereWrapperStartBuilder whereItem() {
        return null;
    }

    public static FromItem fromItem(Class table) {
        return new FromItem(table);
    }

    public static FromItem fromItem(Class table, String aliasName) {
        return new FromItem(table, aliasName);
    }
}
