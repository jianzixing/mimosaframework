package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.DefaultDelete;
import org.mimosaframework.orm.mapping.MappingTable;

public interface DeleteDatabasePorter {
    PorterStructure[] delete(MappingTable table, ModelObject object);

    PorterStructure[] delete(MappingTable table, DefaultDelete delete);

    PorterStructure[] simpleDelete(String table, ModelObject where);
}
