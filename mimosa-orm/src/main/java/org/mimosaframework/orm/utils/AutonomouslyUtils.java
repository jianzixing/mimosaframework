package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.platform.ChangerClassify;
import org.mimosaframework.orm.platform.PorterStructure;
import org.mimosaframework.orm.scripting.BoundSql;
import org.mimosaframework.orm.scripting.DynamicSqlSource;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;

public class AutonomouslyUtils {
    public static PorterStructure parseStructure(SQLDefinedLoader definedLoader, String name, ModelObject parameter) {
        DynamicSqlSource sqlSource = definedLoader.getDynamicSqlSource(name);
        if (sqlSource == null) {
            throw new IllegalArgumentException("没有发现配置文件SQL");
        }

        BoundSql boundSql = sqlSource.getBoundSql(parameter);
        String action = boundSql.getAction();

        PorterStructure structure = null;
        if (action.equalsIgnoreCase("select")) {
            structure = new PorterStructure(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setChangerClassify(ChangerClassify.SELECT);
        } else if (action.equalsIgnoreCase("update")) {
            structure = new PorterStructure(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setChangerClassify(ChangerClassify.UPDATE);
        } else if (action.equalsIgnoreCase("delete")) {
            structure = new PorterStructure(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setChangerClassify(ChangerClassify.DELETE);
        } else if (action.equalsIgnoreCase("insert")) {
            structure = new PorterStructure(boundSql.getSql(), boundSql.getDataPlaceholders());
            structure.setChangerClassify(ChangerClassify.ADD_OBJECT);
        }
        return structure;
    }
}
