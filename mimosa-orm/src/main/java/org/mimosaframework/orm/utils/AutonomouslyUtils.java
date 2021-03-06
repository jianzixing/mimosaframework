package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.platform.JDBCTraversing;
import org.mimosaframework.orm.platform.TypeForRunner;
import org.mimosaframework.orm.scripting.BoundSql;
import org.mimosaframework.orm.scripting.DynamicSqlSource;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;

public class AutonomouslyUtils {
    public static JDBCTraversing parseStructure(SQLDefinedLoader definedLoader, String name, ModelObject parameter) {
        DynamicSqlSource sqlSource = definedLoader.getDynamicSqlSource(name);
        if (sqlSource == null) {
            throw new IllegalArgumentException(I18n.print("not_found_file_sql"));
        }

        BoundSql boundSql = sqlSource.getBoundSql(parameter);
        String action = boundSql.getAction();

        JDBCTraversing structure = null;
        if (action.equalsIgnoreCase("select")) {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setTypeForRunner(TypeForRunner.SELECT);
        } else if (action.equalsIgnoreCase("update")) {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setTypeForRunner(TypeForRunner.UPDATE);
        } else if (action.equalsIgnoreCase("delete")) {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setTypeForRunner(TypeForRunner.DELETE);
        } else if (action.equalsIgnoreCase("insert")) {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setTypeForRunner(TypeForRunner.INSERT);
        } else {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setTypeForRunner(TypeForRunner.OTHER);
        }
        return structure;
    }
}
