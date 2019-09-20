package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MappingGlobalWrapper {
    private Map<Class, MappingTable> mappingTables;
    private Map<MimosaDataSource, MappingDatabase> maps;
    private Map<String, List<MappingTable>> dimensions;

    public void setDataSourceMappingDatabase(Map<MimosaDataSource, MappingDatabase> maps) {
        this.maps = maps;
        if (this.maps != null) {
            Iterator<Map.Entry<MimosaDataSource, MappingDatabase>> iterator = maps.entrySet().iterator();
            while (iterator.hasNext()) {
                if (dimensions == null) {
                    dimensions = new LinkedHashMap<>();
                }
                Map.Entry<MimosaDataSource, MappingDatabase> dbs = iterator.next();
                MappingDatabase db = dbs.getValue();
                if (db != null) {
                    Set<MappingTable> tables = db.getDatabaseTables();
                    if (tables != null) {
                        for (MappingTable table : tables) {
                            if (table.getMappingClass() != null) {
                                String key = table.getMappingClass().getName() + "#" + table.getDatabaseTableName();
                                List<MappingTable> list = dimensions.get(key);
                                if (list == null) {
                                    list = new ArrayList<>();
                                }
                                list.add(table);
                                dimensions.put(key, list);
                            }
                        }
                    }
                }
            }
        }
    }

    public MappingTable getMappingTable(Class c) {
        if (mappingTables != null) {
            return mappingTables.get(c);
        }
        return null;
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

    public MappingTable getDatabaseTable(MimosaDataSource ds, String tableName) {
        if (ds != null) {
            MappingDatabase mappingDatabase = maps.get(ds);
            if (mappingDatabase != null) {
                MappingTable table = mappingDatabase.getDatabaseTable(tableName);
                if (table != null) {
                    return table;
                }
                return null;
            }
        }
        return null;
    }

    public boolean contains(MappingTable table, String fieldName, boolean isFromClass) {
        if (isFromClass) {
            Set<MappingField> fields = table.getMappingFields();
            if (fields != null) {
                for (MappingField field : fields) {
                    if (field.getMappingFieldName().equalsIgnoreCase(fieldName)) {
                        return true;
                    }
                }
            }
        } else {
            Set<MappingField> fields = table.getMappingFields();
            if (fields != null) {
                for (MappingField field : fields) {
                    if (field.getDatabaseColumnName().equalsIgnoreCase(fieldName)) {
                        return true;
                    }
                }
            }
        }
        return false;
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
}
