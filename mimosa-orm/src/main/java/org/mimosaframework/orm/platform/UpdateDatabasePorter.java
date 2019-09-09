package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.DefaultUpdate;
import org.mimosaframework.orm.mapping.MappingTable;

public interface UpdateDatabasePorter {
    PorterStructure[] update(MappingTable table, ModelObject object);

    PorterStructure[] update(MappingTable table, DefaultUpdate update);

    PorterStructure[] simpleUpdate(String table, ModelObject object, ModelObject where);
}
