package org.mimosaframework.orm.merge;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.DefaultJoin;
import org.mimosaframework.orm.criteria.Join;

/**
 * @author yangankang
 */
public class DefaultVariableBeanName implements VariableBeanName {
    @Override
    public String getVarName(Class c) {
        String name = c.getSimpleName();
        return name;
    }

    @Override
    public String getVarName(Join join) {
        if (StringTools.isNotEmpty(((DefaultJoin) join).getAliasName())) {
            return ((DefaultJoin) join).getAliasName();
        }
        return this.getVarName(((DefaultJoin) join).getTable());
    }
}
