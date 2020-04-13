package org.mimosaframework.orm.mapping;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MappingGlobalWrapper {
    /**
     * 标准的映射类信息，理论上这里只包含映射类解析后的信息
     * 不包含数据库表的信息
     */
    private Map<Class, MappingTable> mappingTables;

    public MappingTable getMappingTable(Class c) {
        if (mappingTables != null) {
            return mappingTables.get(c);
        }
        return null;
    }

    public List<MappingTable> getMappingTables() {
        List<MappingTable> mappingTables = new ArrayList<>();
        mappingTables.addAll(this.mappingTables.values());
        return mappingTables;
    }

    public MappingTable getMappingTable(String tableName) {
        Iterator<Map.Entry<Class, MappingTable>> iterator = mappingTables.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, MappingTable> entry = iterator.next();
            MappingTable table = entry.getValue();
            if (table != null && table.getMappingTableName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }

    public void setMappingTables(Set<MappingTable> mappingTables) {
        if (this.mappingTables == null) {
            this.mappingTables = new ConcurrentHashMap<>();
        } else {
            this.mappingTables.clear();
        }

        if (mappingTables != null) {
            Iterator<MappingTable> iterator = mappingTables.iterator();
            while (iterator.hasNext()) {
                MappingTable dbs = iterator.next();
                this.mappingTables.put(dbs.getMappingClass(), dbs);
            }
        }
    }

    public boolean contains(MappingTable table, String fieldName) {
        Set<MappingField> fields = table.getMappingFields();
        if (fields != null) {
            for (MappingField field : fields) {
                if (field.getMappingFieldName().equalsIgnoreCase(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
