package org.mimosaframework.orm.scripting;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.ognl.OgnlException;
import org.mimosaframework.core.ognl.OgnlRuntime;
import org.mimosaframework.core.ognl.PropertyAccessor;

import java.util.HashMap;
import java.util.Map;

public class DynamicContext {

    public static final String PARAMETER_OBJECT_KEY = "_parameter";
    public static final String DATABASE_ID_KEY = "_databaseId";

    static {
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
    }

    private final ContextMap bindings;
    private final StringBuilder sqlBuilder = new StringBuilder();
    private int uniqueNumber = 0;

    public DynamicContext(DefinerConfigure configuration, ModelObject parameterObject) {
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            bindings = new ContextMap(parameterObject);
        } else {
            bindings = new ContextMap(null);
        }
        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
        bindings.put(DATABASE_ID_KEY, configuration.getDataBaseType());
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    public void appendSql(String sql) {
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        return uniqueNumber++;
    }

    static class ContextMap extends HashMap<String, Object> {
        private static final long serialVersionUID = 2977601501966151582L;

        private ModelObject parameterMetaObject;

        public ContextMap(ModelObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }

            if (parameterMetaObject != null) {
                Object object = parameterMetaObject.get(strKey);
                if (object != null) {
                    super.put(strKey, object);
                }

                return object;
            }

            return null;
        }
    }

    static class ContextAccessor implements PropertyAccessor {

        public Object getProperty(Map context, Object target, Object name)
                throws OgnlException {
            Map map = (Map) target;

            Object result = map.get(name);
            if (result != null) {
                return result;
            }

            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }

            return null;
        }

        public void setProperty(Map context, Object target, Object name, Object value)
                throws OgnlException {
            Map map = (Map) target;
            map.put(name, value);
        }
    }
}
