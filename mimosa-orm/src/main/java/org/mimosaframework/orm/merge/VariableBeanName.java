package org.mimosaframework.orm.merge;

import org.mimosaframework.orm.criteria.Join;

/**
 * @author yangankang
 */
public interface VariableBeanName {

    String getVarName(Class c);

    String getVarName(Join join);
}
