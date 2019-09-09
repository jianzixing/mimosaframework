package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpecificMappingDatabase implements MappingDatabase {
    private MimosaDataSource dataSource;

    /**
     * 如果有Class映射类则会在当前容器中，如果只是从数据库读出则当前
     * 容器肯定不会有
     */
    private Map<Class, MappingTable> tables;

    /**
     * 不管是Class映射类还是数据库中存在但没有映射类的表都会存在当前容器中
     */
    private Map<String, MappingTable> tablesByName;

    /**
     * 在运行时初始化，方便快速获取映射表
     */
    private Map<Class, List<MappingTable>> tablesByClasses;
    /**
     * 在运行时初始化，方便快速获取映射表
     * 如果不使用Class的情况，在集群配置表中存在如下配置：
     * <table name="t_user">
     * <split name="t_user_1"></split>
     * <split name="t_user_2"></split>
     * <split name="t_user_3"></split>
     * </table>
     * <p>
     * 则使用API添删改查时只能使用t_user表名，然后对应的分表
     * 名称就存在这里方便使用
     */
    private Map<String, List<MappingTable>> databaseNameTables;

    public SpecificMappingDatabase(MimosaDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addDatabaseTable(MappingTable table) {
        if (tablesByName == null) {
            tablesByName = new LinkedHashMap<>();
        }
        tablesByName.put(table.getDatabaseTableName(), table);
    }

    public void addMappingTable(MappingTable table) {
        if (tables == null) {
            tables = new LinkedHashMap<>();
        }
        tables.put(table.getMappingClass(), table);
    }

    public MappingTable getDatabaseTable(String tableName) {
        return tablesByName.get(tableName);
    }

    @Override
    public Set<MappingTable> getDatabaseTables() {
        if (tablesByName != null) {
            Set<MappingTable> set = new LinkedHashSet<>(tablesByName.size());
            Iterator<Map.Entry<String, MappingTable>> iterator = tablesByName.entrySet().iterator();
            while (iterator.hasNext()) {
                set.add(iterator.next().getValue());
            }
            return set;
        }
        return null;
    }

    @Override
    public MappingTable[] getDatabaseTables(Class c) {
        if (tablesByName != null) {
            if (tablesByClasses == null) {
                synchronized (this) {
                    if (tablesByClasses == null) {
                        tablesByClasses = new LinkedHashMap<>();
                        Iterator<Map.Entry<String, MappingTable>> iterator = tablesByName.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, MappingTable> entry = iterator.next();
                            MappingTable t = entry.getValue();
                            Class oc = t.getMappingClass();
                            List<MappingTable> list = tablesByClasses.get(oc);
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            list.add(t);
                            tablesByClasses.put(t.getMappingClass(), list);
                        }
                    }
                }
            }
            List<MappingTable> list = tablesByClasses.get(c);
            if (list != null) {
                return list.toArray(new MappingTable[]{});
            }
        }
        return null;
    }

    @Override
    public MappingTable[] getDatabaseTables(String dbTableName) {
        if (tablesByName != null) {
            if (databaseNameTables == null) {
                synchronized (this) {
                    if (databaseNameTables == null) {
                        databaseNameTables = new LinkedHashMap<>();
                        Iterator<Map.Entry<String, MappingTable>> iterator = tablesByName.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, MappingTable> entry = iterator.next();
                            MappingTable t = entry.getValue();
                            String tableName = t.getMappingTableName();
                            List<MappingTable> list = databaseNameTables.get(tableName);
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            list.add(t);
                            databaseNameTables.put(tableName, list);
                        }
                    }
                }
            }
            List<MappingTable> list = tablesByClasses.get(dbTableName);
            if (list != null) {
                return list.toArray(new MappingTable[]{});
            }
        }
        return new MappingTable[0];
    }
}
