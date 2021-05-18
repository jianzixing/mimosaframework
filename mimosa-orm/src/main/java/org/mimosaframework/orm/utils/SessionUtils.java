package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.*;

public final class SessionUtils {

    public static void clearModelObject(MappingGlobalWrapper mappingDatabaseWrapper, Class c, ModelObject object) {
        if (c == null) {
            throw new IllegalArgumentException("没有设置要操作的映射类");
        }
        MappingTable tableFromClass = mappingDatabaseWrapper.getMappingTable(c);
        if (tableFromClass == null) {
            throw new IllegalArgumentException("没有找到对应的关系映射[" + c.getName() + "]");
        }

        // 不能清空，需要NULL和空字符串更新和添加
        // object.clearEmpty();

        Set<Object> keys = object.keySet();
        List<Object> removed = new ArrayList<>();
        for (Object o : keys) {
            if (!mappingDatabaseWrapper.contains(tableFromClass, String.valueOf(o))) {
                removed.add(o);
            }
        }
        for (Object o : removed) {
            object.remove(o);
        }
    }

    public static boolean checkPrimaryKey(List<MappingField> primaryKey, ModelObject object) {
        if (primaryKey == null) {
            return false;
        }
        for (MappingField mappingField : primaryKey) {
            if (!object.containsKey(mappingField.getMappingFieldName())
                    || !object.isNotEmpty(mappingField.getMappingFieldName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否有相同引用  如果存在相同引用的ModelObject就克隆一个
     *
     * @param objects
     */
    public static void checkReference(List<ModelObject> objects) {
        if (objects != null && objects.size() > 0) {
            Set<ModelObject> set = new HashSet<>();
            for (ModelObject object : objects) {
                for (ModelObject m : objects) {
                    if (object == m) {
                        set.add(object);
                    }
                }
            }
            for (ModelObject o : set) {
                objects.set(objects.indexOf(o), (ModelObject) o.clone());
            }
        }
    }

    public static void applyAutoIncrementValue(MappingTable mappingTable, Long id, ModelObject objSource) {
        Set<MappingField> fields = mappingTable.getMappingFields();
        if (fields != null && id != null && objSource != null) {
            for (MappingField f : fields) {
                if (f.isMappingFieldAutoIncrement()) {
                    objSource.put(f.getMappingFieldName(), id);
                }
            }
        }
    }

    public static void applyAutoIncrementValue(MappingTable mappingTable, List<Long> ids, List<ModelObject> objSource) {
        if (ids != null && objSource != null && ids.size() == objSource.size()) {
            Set<MappingField> fields = mappingTable.getMappingFields();
            if (fields != null && ids != null && objSource != null) {
                for (MappingField f : fields) {
                    if (f.isMappingFieldAutoIncrement()) {
                        int i = 0;
                        for (Long id : ids) {
                            ModelObject object = objSource.get(i);
                            object.put(f.getMappingFieldName(), id);
                            i++;
                        }
                    }
                }
            }
        }
    }

    public static Update buildUpdateByModel(MappingTable mappingTable, ModelObject object) {
        Class c = mappingTable.getMappingClass();
        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        Update update = Criteria.update(c);
        Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();

            boolean isPk = false;
            for (MappingField field : pks) {
                if (field.getMappingFieldName().equals(key)) {
                    isPk = true;
                    update.eq(String.valueOf(key), entry.getValue());
                    break;
                }
            }
            if (!isPk) {
                MappingField mappingField = mappingTable.getMappingFieldByJavaName(String.valueOf(key));
                if (mappingField != null) {
                    update.set(String.valueOf(key), entry.getValue());
                }
            }
        }
        return update;
    }

    public static Delete buildDeleteByModel(MappingTable mappingTable, ModelObject object) {
        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        Class c = mappingTable.getMappingClass();
        Delete delete = Criteria.delete(c);
        for (MappingField field : pks) {
            delete.eq(field.getMappingFieldName(), object.get(field.getMappingFieldName()));
        }
        return delete;
    }

    public static void clearPkZeroModelObject(MappingGlobalWrapper mappingGlobalWrapper, Class c, ModelObject obj) {
        MappingTable mappingTable = mappingGlobalWrapper.getMappingTable(c);
        if (mappingTable != null) {
            List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
            if (pks != null && pks.size() > 0) {
                for (MappingField pk : pks) {
                    Object object = obj.getString(pk.getMappingFieldName());
                    if (object != null && object.equals(0)) {
                        obj.remove(pk.getMappingFieldName());
                    }
                }
            }
        }
    }
}
