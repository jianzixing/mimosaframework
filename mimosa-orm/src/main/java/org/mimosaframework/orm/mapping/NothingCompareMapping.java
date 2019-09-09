package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.sql.SQLException;
import java.util.*;

public class NothingCompareMapping implements StartCompareMapping {
    protected FetchDatabaseMapping fetchDatabaseMapping;
    protected ActionDataSourceWrapper dataSourceWrapper;
    protected Set<Class> mappingClasses;
    protected MappingNamedConvert convert;
    protected boolean isUpdateDatabaseTable = false;
    private Set<MappingTable> mappingTables;

    public NothingCompareMapping(Set<Class> mappingClasses,
                                 ActionDataSourceWrapper dataSourceWrapper,
                                 MappingNamedConvert convert) {
        this.dataSourceWrapper = dataSourceWrapper;
        this.mappingClasses = mappingClasses;
        this.convert = convert;

        if (mappingClasses != null) {
            List<String> names = new ArrayList<>(mappingClasses.size());
            for (Class c : mappingClasses) {
                if (names.contains(c.getSimpleName())) {
                    throw new IllegalArgumentException("不允许映射类" + c.getSimpleName() + "有相同的名称");
                }
                names.add(c.getSimpleName());
            }
        }
    }

    @Override
    public Map<MimosaDataSource, NotMatchObject> doMapping() throws SQLException {
        if (mappingClasses != null) {

            // 如果存在先清空
            if (mappingTables != null) {
                mappingTables.clear();
            }

            for (Class c : mappingClasses) {
                if (mappingTables == null) {
                    mappingTables = new LinkedHashSet<>();
                }
                DisassembleMappingClass disassembleMappingClass = new DefaultDisassembleMappingClass(c, convert);
                MappingTable mappingTable = disassembleMappingClass.getMappingTable();
                mappingTables.add(mappingTable);
            }
        }

        MappingTableWrapper tableWrapper = new MappingTableWrapper(mappingTables);

        fetchDatabaseMapping = new JDBCFetchDatabaseMapping();
        fetchDatabaseMapping.setDataSourceWrapper(dataSourceWrapper);
        Set<MimosaDataSource> dataSources = fetchDatabaseMapping.getUseDataSources();
        Map<MimosaDataSource, NotMatchObject> notMatchObjects = new LinkedHashMap<>();
        if (dataSources != null) {
            for (MimosaDataSource dataSource : dataSources) {
                if (dataSource == dataSourceWrapper.getDataSource()) {
                    // 检查全部表映射
                    MappingDatabase mappingDatabase = fetchDatabaseMapping.getDatabaseMapping(dataSource);
                    if (mappingDatabase != null) {
                        NotMatchObject missing = tableWrapper.getMissingObject(dataSource, mappingDatabase);
                        notMatchObjects.put(dataSource, missing);
                    }
                } else {
                    // 检查当前支持的表映射
                    List<SupposedTables> supposedTables = fetchDatabaseMapping.getSupposedMappingTableByDataSource(dataSource);
                    Set<MappingTable> clusterTables = null;
                    if (supposedTables != null) {
                        clusterTables = new LinkedHashSet<>();
                        for (SupposedTables t : supposedTables) {
                            // 通过集群配置的 Class 和 tableName 获得映射类中的对应的MappingTable
                            // 如果有则表明这个配置是在映射类中的然后检查表和字段
                            // 如果没有则表明这个配置的表不在映射类中，不做处理
                            // 因为映射类的映射是单向的，只会从映射类到表中
                            // 如果是分表则 mappingTableName 就是分表的名称，一个SupposedTables对应一个MappingTable
                            MappingTable hasTable = tableWrapper.getCloneMappingTable(t);
                            if (hasTable != null) {
                                clusterTables.add(hasTable);
                            }
                        }

                        MappingDatabase mappingDatabase = fetchDatabaseMapping.getDatabaseMapping(dataSource);
                        if (mappingDatabase != null) {
                            MappingTableWrapper myWrapper = new MappingTableWrapper(clusterTables);
                            // 这里还需要判断分表的名称比如A表的 A_1 , A_2 , A_3
                            // 如果有分表则对应的 MappingTable 中的 mappingTableName 就是分表名称
                            NotMatchObject missing = myWrapper.getMissingObject(dataSource, mappingDatabase);
                            notMatchObjects.put(dataSource, missing);
                        }
                    }
                }
            }
        }
        return notMatchObjects;
    }

    /**
     * 开始合并所有的MappingDatabase
     * 从Class获得的MappingTable合并到从数据库获得MappingTable中去
     *
     * @return
     * @throws SQLException
     */
    public MappingGlobalWrapper getWholeMappingDatabase() throws SQLException {
        if (fetchDatabaseMapping == null) {
            fetchDatabaseMapping = new JDBCFetchDatabaseMapping();
            fetchDatabaseMapping.setDataSourceWrapper(dataSourceWrapper);
        }
        Map<MimosaDataSource, MappingDatabase> maps = fetchDatabaseMapping.loading();
        // 加载完所有数据库的信息后，将映射类的MappingTable合并到每一个数据库对应的MappingDatabase中去
        if (mappingTables == null && mappingClasses != null) {
            for (Class c : mappingClasses) {
                if (mappingTables == null) {
                    mappingTables = new LinkedHashSet<>();
                }
                DisassembleMappingClass disassembleMappingClass = new DefaultDisassembleMappingClass(c, convert);
                MappingTable mappingTable = disassembleMappingClass.getMappingTable();
                mappingTables.add(mappingTable);
            }
        }

        // 开始合并映射类和数据库表信息
        if (mappingTables != null) {
            Iterator<Map.Entry<MimosaDataSource, MappingDatabase>> iterator = maps.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<MimosaDataSource, MappingDatabase> next = iterator.next();
                MimosaDataSource ds = next.getKey();
                MappingDatabase database = next.getValue();

                if (database != null) {
                    List<SupposedTables> supposedTables = fetchDatabaseMapping.getSupposedMappingTableByDataSource(ds);
                    // 如果是默认的数据库连接则直接匹配所有的映射类
                    if (dataSourceWrapper.getDataSource() == ds) {
                        Set<MappingTable> tables = database.getDatabaseTables();
                        if (tables != null) {
                            for (MappingTable mpTable : mappingTables) {
                                for (MappingTable dbTable : tables) {
                                    if (mpTable.getMappingTableName().equalsIgnoreCase(dbTable.getDatabaseTableName())) {
                                        dbTable.applyFromClassMappingTable(mpTable);
                                        database.addMappingTable(dbTable);
                                        database.addDatabaseTable(dbTable);
                                    }
                                }
                            }
                        }
                    }

                    // 如果有配置集群表就匹配所有集群表
                    // 如果没有配置集群表且也不是当前数据库连接则什么都不做
                    if (supposedTables != null && supposedTables.size() > 0) {
                        Set<MappingTable> tables = database.getDatabaseTables();
                        if (tables != null) {
                            for (MappingTable mpTable : mappingTables) {
                                for (SupposedTables spTable : supposedTables) {

                                    if (mpTable.getMappingClass() == spTable.getTableClass()
                                            || mpTable.getMappingTableName().equalsIgnoreCase(spTable.getTableName())) {

                                        for (MappingTable dbTable : tables) {
                                            if (dbTable.getDatabaseTableName().equalsIgnoreCase(spTable.getSplitName())
                                                    || dbTable.getDatabaseTableName().equalsIgnoreCase(spTable.getTableName())) {
                                                dbTable.applyFromClassMappingTable(mpTable);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Map<Class, MappingTable> classTables = new HashMap<>();
        if (mappingTables != null) {
            for (MappingTable t : mappingTables) {
                classTables.put(t.getMappingClass(), t);
            }
        }
        return new MappingGlobalWrapper(classTables, maps);
    }
}
