package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.DefaultConfiguration;

public interface TableCompare {
    void doMapping(DefaultConfiguration configuration, CompareMapping compareMapping);
}
