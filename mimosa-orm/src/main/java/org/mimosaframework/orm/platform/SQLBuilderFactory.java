package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.utils.DatabaseTypes;

/**
 * @author yangankang
 */
public class SQLBuilderFactory {

    public static SQLBuilder createSQLBuilder(DatabaseTypes types) {
        if (types == DatabaseTypes.DB2) return createQMSQLBuilder();
        if (types == DatabaseTypes.ORACLE) return createQMSQLBuilder();
        if (types == DatabaseTypes.POSTGRESQL) return createQMSQLBuilder();
        if (types == DatabaseTypes.SQLITE) return createQMSQLBuilder();
        if (types == DatabaseTypes.SQL_SERVER) return createBraceSQLBuilder();
        return createSQLBuilder();
    }

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
