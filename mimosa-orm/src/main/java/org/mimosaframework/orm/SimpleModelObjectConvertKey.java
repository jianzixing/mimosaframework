package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yangankang
 */
public class SimpleModelObjectConvertKey implements ModelObjectConvertKey {
    private MappingGlobalWrapper mappingGlobalWrapper;

    @Override
    public ModelObject convert(Class tableClass, ModelObject object) {
        if (tableClass != null) {
            MappingTable mappingTable = null;
            if (tableClass != null) {
                mappingTable = this.mappingGlobalWrapper.getMappingTable(tableClass);
            }
            Set<Object> objectSet = object.keySet();
            Set<Object> keyset = new LinkedHashSet<Object>();
            keyset.addAll(objectSet);
            for (Object o : keyset) {
                String key = null;
                if (mappingTable != null) {
                    MappingField mappingField = mappingTable.getMappingFieldByName(String.valueOf(o));
                    if (mappingField != null) {
                        key = mappingField.getMappingColumnName();
                    }
                }
                if (key != null && !o.equals(key)) {
                    Object value = object.get(o);
                    object.remove(o);
                    object.put(key, value);
                }
            }
        }
        return object;
    }

    @Override
    public ModelObject reconvert(Class tableClass, ModelObject object) {
        if (tableClass != null) {
            MappingTable mappingTable = null;
            if (tableClass != null) {
                mappingTable = this.mappingGlobalWrapper.getMappingTable(tableClass);
            }
            Set<Object> objectSet = object.keySet();
            Set<Object> keyset = new LinkedHashSet<Object>();
            keyset.addAll(objectSet);
            for (Object o : keyset) {
                String key = null;
                if (mappingTable != null) {
                    MappingField mappingField = mappingTable.getMappingFieldByName(String.valueOf(o));
                    if (mappingField != null) {
                        key = mappingField.getMappingFieldName();
                    }
                }

                if (key != null && !o.equals(key)) {
                    Object value = object.get(o);
                    object.remove(o);
                    object.put(key, value);
                }
            }
        }
        return object;
    }

    @Override
    public List<ModelObject> convert(Class tableClass, List<ModelObject> objects) {
        if (objects != null && objects.size() > 0) {
            List<ModelObject> list = new ArrayList<>();
            for (ModelObject o : objects) {
                list.add(this.convert(tableClass, o));
            }
            return list;
        }
        return null;
    }

    @Override
    public List<ModelObject> reconvert(Class tableClass, List<ModelObject> objects) {
        if (objects != null && objects.size() > 0) {
            List<ModelObject> list = new ArrayList<>();
            for (ModelObject o : objects) {
                list.add(this.reconvert(tableClass, o));
            }
            return list;
        }
        return null;
    }

    @Override
    public void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
    }
}
