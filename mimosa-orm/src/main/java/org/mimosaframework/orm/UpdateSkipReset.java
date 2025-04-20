package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.List;

public interface UpdateSkipReset {
    void skip(ModelObject object, MappingTable mappingTable, List<String> usedFields);
}
