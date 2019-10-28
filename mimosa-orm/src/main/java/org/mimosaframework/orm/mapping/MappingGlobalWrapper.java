package org.mimosaframework.orm.mapping;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MappingGlobalWrapper {
    /**
     * 分库分表时不同的数据源上的不同映射表
     * 以数据源为尺度的映射表信息，这里的映射表包含映射类和数据库表信息
     */
    private Map<MimosaDataSource, Set<MappingTable>> mappingDatabaseTables;

    /**
     * 每个数据源的所有的表，即使没有映射也存储一下，这里不包含映射类信息
     * 单纯就是读取的表信息
     * <p>
     * 在自定义的分表自增ID策略时使用到。
     * 可以用作查询每个数据源的每个表信息
     */
    private Map<MimosaDataSource, MappingDatabase> dataSourceTables;

    /**
     * 以表尺度的映射类信息，这里的映射类信息包含从数据库读取的表信息
     * key是已映射类名+数据库表名作为键值，如果存在分表信息则值内容
     * 是每个表的信息，比如表t分了三份则，set中有的MappingTable
     * 的数据库对比信息中的表名称就是t_1 , t_2 , t_3
     */
    private Map<String, Set<MappingTable>> dimensions;

    /**
     * 标准的映射类信息，理论上这里只包含映射类解析后的信息
     * 不包含数据库表的信息
     */
    private Map<Class, MappingTable> mappingTables;

    public void setDataSourceMappingDatabase(Map<MimosaDataSource, MappingDatabase> maps) {
        this.dataSourceTables = maps;
        if (this.dataSourceTables != null) {
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
                                Set<MappingTable> list = dimensions.get(key);
                                if (list == null) {
                                    list = new HashSet<>();
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

    /**
     * 只获取一个一样的mappingTable即可
     *
     * @param tableClass
     * @param tableName
     * @return
     */
    public MappingTable getMappingDatabaseTable(Class tableClass, String tableName) {
        if (StringTools.isNotEmpty(tableName)) {
            Set<MappingTable> tables = dimensions.get(tableClass.getName() + "#" + tableName);
            if (tables != null && tables.size() > 0) {
                return tables.iterator().next();
            }
        } else {
            return this.mappingTables.get(tableClass);
        }
        return null;
    }

    public MappingTable getMappingDatabaseTable(MimosaDataSource dataSource, String databaseTableName) {
        Set<MappingTable> mappingTables = this.mappingDatabaseTables.get(dataSource);
        if (mappingTables != null) {
            for (MappingTable mappingTable : mappingTables) {
                if (mappingTable.getDatabaseTableName().equalsIgnoreCase(databaseTableName)) {
                    return mappingTable;
                }
            }
        }
        return null;
    }

    public MappingTable getDatabaseTable(MimosaDataSource dataSource, String databaseTableName) {
        MappingDatabase mappingDatabase = this.dataSourceTables.get(dataSource);
        if (mappingDatabase != null) {
            Set<MappingTable> mappingTables = mappingDatabase.getDatabaseTables();
            if (mappingTables != null) {
                for (MappingTable mappingTable : mappingTables) {
                    if (mappingTable.getDatabaseTableName().equalsIgnoreCase(databaseTableName)) {
                        return mappingTable;
                    }
                }
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
        if (this.mappingDatabaseTables != null && wrapper != null) {
            MimosaDataSource dataSource = wrapper.getDataSource();
            return this.getSingleDatabaseTable(dataSource, c);
        }
        return null;
    }

    public MappingTable getSingleDatabaseTable(MimosaDataSource ds, Class c) {
        if (ds != null) {
            Set<MappingTable> mappingTables = this.mappingDatabaseTables.get(ds);
            if (mappingTables != null) {
                for (MappingTable mappingTable : mappingTables) {
                    if (mappingTable.getMappingClass() == c) {
                        return mappingTable;
                    }
                }
                return null;
            }
        }
        return null;
    }

    public List<MappingTable> getMappingDatabaseTable(Class c) {
        if (this.mappingDatabaseTables != null) {
            Iterator<Map.Entry<MimosaDataSource, Set<MappingTable>>> dbs = this.mappingDatabaseTables.entrySet().iterator();
            List<MappingTable> tableList = new ArrayList<>();
            while (dbs.hasNext()) {
                Set<MappingTable> tables = dbs.next().getValue();
                if (tables != null && tables.size() > 0) {
                    for (MappingTable m : tables) {
                        if (m.getMappingClass() == c) {
                            tableList.add(m);
                        }
                    }
                }
            }
            return tableList;
        }
        return null;
    }

    public void setDataSourceTables(Map<MimosaDataSource, MappingDatabase> dataSourceTables) {
        this.dataSourceTables = dataSourceTables;
    }

    public void setDataSourceMappingTables(Map<MimosaDataSource, Set<MappingTable>> mappingTables) {
        this.mappingDatabaseTables = mappingTables;
        if (dimensions != null) dimensions.clear();

        Iterator<Map.Entry<MimosaDataSource, Set<MappingTable>>> iterator = mappingTables.entrySet().iterator();
        while (iterator.hasNext()) {
            if (dimensions == null) {
                dimensions = new LinkedHashMap<>();
            }
            Map.Entry<MimosaDataSource, Set<MappingTable>> dbs = iterator.next();
            Set<MappingTable> tables = dbs.getValue();
            if (tables != null) {
                for (MappingTable table : tables) {
                    if (table.getMappingClassName() != null) {
                        String key = table.getMappingClass().getName() + "#" + table.getDatabaseTableName();
                        Set<MappingTable> list = dimensions.get(key);
                        if (list == null) {
                            list = new LinkedHashSet<>();
                        }
                        list.add(table);
                        dimensions.put(key, list);
                    }
                }
            }
        }
    }
}
