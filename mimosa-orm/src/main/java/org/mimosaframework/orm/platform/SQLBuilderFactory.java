package org.mimosaframework.orm.platform;

/**
 * @author yangankang
 */
public class SQLBuilderFactory {

    public static SQLBuilder createSQLBuilder() {
        SQLBuilder sqlBuilder = new CommonSQLBuilder();
        return sqlBuilder;
    }

    public static SQLBuilder createEmptyRuleSQLBuilder() {
        SQLBuilder sqlBuilder = new CommonSQLBuilder();
        sqlBuilder.setTableFieldReplaceRule("", "");
        return sqlBuilder;
    }

    public static SQLBuilder createQMSQLBuilder() {
        SQLBuilder sqlBuilder = new CommonSQLBuilder();
        sqlBuilder.setTableFieldReplaceRule("\"", "\"");
        return sqlBuilder;
    }

    public static SQLBuilder createBraceSQLBuilder() {
        SQLBuilder sqlBuilder = new CommonSQLBuilder();
        sqlBuilder.setTableFieldReplaceRule("[", "]");
        return sqlBuilder;
    }
}
