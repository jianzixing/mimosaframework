package org.mimosaframework.orm.scripting.tags;

import org.mimosaframework.orm.scripting.DynamicContext;
import org.mimosaframework.orm.scripting.ExpressionEvaluator;
import org.mimosaframework.orm.scripting.SqlNode;

public class IfSqlNode implements SqlNode {
    private ExpressionEvaluator evaluator;
    private String test;
    private SqlNode contents;

    public IfSqlNode(SqlNode contents, String test) {
        this.test = test;
        this.contents = contents;
        this.evaluator = new ExpressionEvaluator();
    }

    public boolean apply(DynamicContext context) {
        if (evaluator.evaluateBoolean(test, context.getBindings())) {
            contents.apply(context);
            return true;
        }
        return false;
    }

}
