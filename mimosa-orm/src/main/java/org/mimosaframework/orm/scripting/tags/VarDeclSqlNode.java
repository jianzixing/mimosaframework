package org.mimosaframework.orm.scripting.tags;

import org.mimosaframework.orm.scripting.DynamicContext;
import org.mimosaframework.orm.scripting.OgnlCache;
import org.mimosaframework.orm.scripting.SqlNode;

public class VarDeclSqlNode implements SqlNode {

    private final String name;
    private final String expression;

    public VarDeclSqlNode(String var, String exp) {
        name = var;
        expression = exp;
    }

    public boolean apply(DynamicContext context) {
        final Object value = OgnlCache.getValue(expression, context.getBindings());
        context.bind(name, value);
        return true;
    }

}
