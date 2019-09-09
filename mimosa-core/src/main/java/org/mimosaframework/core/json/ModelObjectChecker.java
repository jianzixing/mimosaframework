package org.mimosaframework.core.json;

import org.mimosaframework.core.exception.ModelCheckerException;

/**
 * 用来校验检查ModelObject的每一个值是否正确
 *
 * @author yangankang
 */
public interface ModelObjectChecker {

    void checker(ModelObject object, Object[] removed) throws ModelCheckerException;

    void checkerUpdate(ModelObject object, Object[] removed) throws ModelCheckerException;
}
