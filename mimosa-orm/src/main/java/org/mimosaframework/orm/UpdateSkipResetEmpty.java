package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.Set;

public class UpdateSkipResetEmpty implements UpdateSkipReset {
    @Override
    public void skip(ModelObject object, MappingTable mappingTable) {
        if (object != null && mappingTable != null) {
            Set<MappingField> fields = mappingTable.getMappingFields();
            if (fields != null) {
                for (MappingField field : fields) {
                    if (field.isMappingFieldTimeForUpdate()
                            || !field.getMappingFieldAnnotation().extCanUpdate()) {
                        object.remove(field.getMappingFieldName());
                    }
                }
            }
        }
    }
}
