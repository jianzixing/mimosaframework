package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.utils.DatabaseType;

/**
 * @author yangankang
 */
public class SQLBuilderFactory {

    public static SQLBuilder createSQLBuilder(DatabaseType types) {
        if (types == DatabaseType.DB2) return createQMSQLBuilder();
        if (types == DatabaseType.ORACLE) return createQMSQLBuilder();
        if (types == DatabaseType.POSTGRESQL) return createQMSQLBuilder();
        if (types == DatabaseType.SQLITE) return createQMSQLBuilder();
        if (types == DatabaseType.SQL_SERVER) return createBraceSQLBuilder();
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
