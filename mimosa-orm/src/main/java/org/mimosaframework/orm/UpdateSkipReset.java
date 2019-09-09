package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingTable;

public interface UpdateSkipReset {
    void skip(ModelObject object, MappingTable mappingTable);
}
