package org.mimosaframework.orm.scripting;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.scripting.tags.MixedSqlNode;

public class DynamicSqlSource implements SqlSource {

    private DefinerConfigure configuration;
    private SqlNode rootSqlNode;

    public DynamicSqlSource(DefinerConfigure configuration, SqlNode rootSqlNode) {
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
    }

    public BoundSql getBoundSql(ModelObject parameterObject) {
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        BoundSql boundSql = sqlSourceParser.parse(context.getSql(), context.getBindings());
        if (rootSqlNode instanceof MixedSqlNode) {
            boundSql.setAction(((MixedSqlNode) rootSqlNode).getAction());
        }

        return boundSql;
    }
}
