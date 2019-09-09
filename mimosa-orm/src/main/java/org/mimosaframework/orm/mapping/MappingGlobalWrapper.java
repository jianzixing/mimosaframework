package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.util.*;

public class MappingGlobalWrapper {
    private Map<Class, MappingTable> mappingTables;
    private Map<MimosaDataSource, MappingDatabase> maps;
    private Map<String, List<MappingTable>> dimensions;

    public MappingGlobalWrapper(Map<Class, MappingTable> mappingTables) {
        this.mappingTables = mappingTables;
    }

    public MappingGlobalWrapper(Map<Class, MappingTable> mappingTables, Map<MimosaDataSource, MappingDatabase> maps) {
        this.mappingTables = mappingTables;
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

    /**
     * 只获取一个一样的mappingTable即可
     *
     * @param c
     * @param tableName
     * @return
     */
    public MappingTable getDatabaseTable(Class c, String tableName) {
        List<MappingTable> tables = dimensions.get(c.getName() + "#" + tableName);
        if (tables != null) {
            return tables.get(0);
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

    /**
     * 获取单机时的数据库表，如果是单机时数据库表不可能会有一个映射类对应多个表的情况
     *
     * @param wrapper
     * @param c
     * @return
     */
    public MappingTable getSingleDatabaseTable(ActionDataSourceWrapper wrapper, Class c) {
        if (maps != null && wrapper != null) {
            MimosaDataSource dataSource = wrapper.getDataSource();
            return this.getSingleDatabaseTable(dataSource, c);
        }
        return null;
    }

    public MappingTable getSingleDatabaseTable(MimosaDataSource ds, Class c) {
        if (ds != null) {
            MappingDatabase mappingDatabase = maps.get(ds);
            if (mappingDatabase != null) {
                MappingTable[] tables = mappingDatabase.getDatabaseTables(c);
                if (tables != null && tables.length > 0) {
                    return tables[0];
                }
                return null;
            }
        }
        return null;
    }

    public List<MappingTable> getDatabaseTable(Class c) {
        Iterator<Map.Entry<MimosaDataSource, MappingDatabase>> dbs = maps.entrySet().iterator();
        List<MappingTable> tableList = new ArrayList<>();
        while (dbs.hasNext()) {
            MappingDatabase mappingDatabase = dbs.next().getValue();
            if (mappingDatabase != null) {
                MappingTable[] tables = mappingDatabase.getDatabaseTables(c);
                if (tables != null && tables.length > 0) {
                    for (MappingTable m : tables) {
                        tableList.add(m);
                    }
                }
            }
        }
        return tableList;
    }
}
