package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.List;

public interface InsertDatabasePorter {
    PorterStructure[] insert(MappingTable table, ModelObject object);

    PorterStructure[] inserts(MappingTable table, List<ModelObject> objects);

    PorterStructure[] simpleInsert(String table, ModelObject object);
}
