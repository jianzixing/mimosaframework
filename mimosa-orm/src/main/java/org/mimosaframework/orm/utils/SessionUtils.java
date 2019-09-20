package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.criteria.DefaultJoin;
import org.mimosaframework.orm.criteria.DefaultQuery;
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
            if (!mappingDatabaseWrapper.contains(tableFromClass, String.valueOf(o), true)) {
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

    public static Map<Object, MappingTable> getUsedMappingTable(MappingGlobalWrapper mappingGlobalWrapper,
                                                                DefaultQuery query,
                                                                MimosaDataSource dataSource,
                                                                String databaseTableName) {
        Class c = query.getTableClass();
        List innerJoins = query.getInnerJoin();
        List leftJoins = query.getLeftJoin();

        int totalAliasTables = 0;
        if (innerJoins != null) totalAliasTables += innerJoins.size();
        if (leftJoins != null) totalAliasTables += leftJoins.size();

        MappingTable table = mappingGlobalWrapper.getDatabaseTable(dataSource, databaseTableName);
        if (table != null) {
            Map<Object, MappingTable> tables = new LinkedHashMap<>(totalAliasTables + 1);
            tables.put(query, table);
            if (innerJoins != null) {
                for (Object join : innerJoins) {
                    DefaultJoin j = (DefaultJoin) join;
                    MappingTable joinTable = mappingGlobalWrapper.getMappingTable(j.getTable());
                    if (joinTable != null) {
                        tables.put(join, joinTable);
                    } else {
                        throw new IllegalArgumentException("没有找到和" + j.getTable().getSimpleName() + "对应的数据库映射表");
                    }
                }
                for (Object join : leftJoins) {
                    DefaultJoin j = (DefaultJoin) join;
                    MappingTable joinTable = mappingGlobalWrapper.getMappingTable(j.getTable());
                    if (joinTable != null) {
                        tables.put(join, joinTable);
                    } else {
                        throw new IllegalArgumentException("没有找到和" + j.getTable().getSimpleName() + "对应的数据库映射表");
                    }
                }
            }

            return tables;
        } else {
            throw new IllegalArgumentException("没有找到和" + c.getSimpleName() + "对应的数据库映射表");
        }
    }

    public static Map<Object, MappingTable> getUsedMappingTable(MappingGlobalWrapper mappingGlobalWrapper, DefaultQuery query) {
        Class c = query.getTableClass();
        List innerJoins = query.getInnerJoin();
        List leftJoins = query.getLeftJoin();

        int totalAliasTables = 0;
        if (innerJoins != null) totalAliasTables += innerJoins.size();
        if (leftJoins != null) totalAliasTables += leftJoins.size();

        MappingTable table = mappingGlobalWrapper.getMappingTable(c);
        if (table != null) {
            Map<Object, MappingTable> tables = new LinkedHashMap<>(totalAliasTables + 1);
            tables.put(query, table);
            if (innerJoins != null) {
                for (Object join : innerJoins) {
                    DefaultJoin j = (DefaultJoin) join;
                    MappingTable joinTable = mappingGlobalWrapper.getMappingTable(j.getTable());
                    if (joinTable != null) {
                        tables.put(join, joinTable);
                    } else {
                        throw new IllegalArgumentException("没有找到和" + j.getTable().getSimpleName() + "对应的数据库映射表");
                    }
                }
                for (Object join : leftJoins) {
                    DefaultJoin j = (DefaultJoin) join;
                    MappingTable joinTable = mappingGlobalWrapper.getMappingTable(j.getTable());
                    if (joinTable != null) {
                        tables.put(join, joinTable);
                    } else {
                        throw new IllegalArgumentException("没有找到和" + j.getTable().getSimpleName() + "对应的数据库映射表");
                    }
                }
            }

            return tables;
        } else {
            throw new IllegalArgumentException("没有找到和" + c.getSimpleName() + "对应的数据库映射表");
        }
    }

    public static void processQueryExcludes(MappingGlobalWrapper mappingGlobalWrapper, DefaultQuery query) {
        Map<Class, List<String>> excludes = query.getExcludes();
        if (excludes != null) {
            for (Map.Entry<Class, List<String>> entry : excludes.entrySet()) {
                Class c = entry.getKey();
                List<String> fields = entry.getValue();
                if (c != null && fields != null && fields.size() > 0) {
                    MappingTable mappingTable = mappingGlobalWrapper.getMappingTable(c);
                    if (mappingTable != null) {
                        Set<MappingField> mappingFields = mappingTable.getMappingFields();
                        if (mappingFields != null) {
                            List<String> queryFields = new ArrayList<>();
                            for (MappingField field : mappingFields) {
                                String fieldName = field.getMappingFieldName();
                                if (!fields.contains(fieldName)) {
                                    queryFields.add(fieldName);
                                }
                            }
                            query.fields(c, queryFields);
                        }
                    }
                }
            }
        }
    }
}
