package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.DefaultConfiguration;
import org.mimosaframework.orm.exception.ContextException;

public interface TableCompare {
    void doMapping(DefaultConfiguration configuration, CompareMapping compareMapping) throws ContextException;
}
