package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.platform.TypeForRunner;
import org.mimosaframework.orm.platform.JDBCTraversing;
import org.mimosaframework.orm.scripting.BoundSql;
import org.mimosaframework.orm.scripting.DynamicSqlSource;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;

public class AutonomouslyUtils {
    public static JDBCTraversing parseStructure(SQLDefinedLoader definedLoader, String name, ModelObject parameter) {
        DynamicSqlSource sqlSource = definedLoader.getDynamicSqlSource(name);
        if (sqlSource == null) {
            throw new IllegalArgumentException("没有发现配置文件SQL");
        }

        BoundSql boundSql = sqlSource.getBoundSql(parameter);
        String action = boundSql.getAction();

        JDBCTraversing structure = null;
        if (action.equalsIgnoreCase("select")) {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setChangerClassify(TypeForRunner.SELECT);
        } else if (action.equalsIgnoreCase("update")) {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setChangerClassify(TypeForRunner.UPDATE);
        } else if (action.equalsIgnoreCase("delete")) {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setChangerClassify(TypeForRunner.DELETE);
        } else if (action.equalsIgnoreCase("insert")) {
            structure = new JDBCTraversing(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setChangerClassify(TypeForRunner.ADD_OBJECT);
        }
        return structure;
    }
}
