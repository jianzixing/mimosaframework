package org.mimosaframework.orm.scripting;

import org.mimosaframework.orm.platform.SQLDataPlaceholder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlSourceBuilder {
    private DefinerConfigure configure;

    public SqlSourceBuilder(DefinerConfigure configure) {
        this.configure = configure;
    }

    public BoundSql parse(String sql, Map<String, Object> bind) {
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler(this.configure, sql, bind);
        GenericTokenParser parser = new GenericTokenParser("#{", "}", tokenHandler);
        sql = parser.parse(sql);

        if (sql != null) {
            sql = sql.replaceAll("\\s{2,}", " ");
            sql = sql.replaceAll("\\n", "");
        }

        tokenHandler.sql = sql;

        return tokenHandler.getBoundSql();
    }

    class ParameterMappingTokenHandler implements TokenHandler {
        private List<SQLDataPlaceholder> dataPlaceholders = new ArrayList<>();
        private Map<String, Object> bind;
        private DefinerConfigure configure;
        private String sql;

        public ParameterMappingTokenHandler(DefinerConfigure configure, String sql, Map<String, Object> bind) {
            this.bind = bind;
            this.configure = configure;
            this.sql = sql;
        }

        @Override
        public String handleToken(String content) {
            SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
            placeholder.setName(content);
            placeholder.setValue(OgnlCache.getValue(content, bind));
            dataPlaceholders.add(placeholder);
            return "?";
        }

        public BoundSql getBoundSql() {
            BoundSql boundSql = new BoundSql(sql);
            boundSql.setDataPlaceholders(this.dataPlaceholders);
            return boundSql;
        }
    }

}

