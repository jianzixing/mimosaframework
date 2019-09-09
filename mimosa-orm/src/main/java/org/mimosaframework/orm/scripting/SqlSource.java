package org.mimosaframework.orm.scripting;

import org.mimosaframework.core.json.ModelObject;

public interface SqlSource {
    BoundSql getBoundSql(ModelObject parameterObject);
}
