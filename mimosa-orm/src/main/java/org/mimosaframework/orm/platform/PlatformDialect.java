package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.sql.StructureBuilder;
import org.mimosaframework.orm.sql.stamp.KeyColumnType;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlatformDialect {
    private Map<KeyColumnType, ColumnType> columnTypes = new HashMap<>();
    private DataSourceWrapper dataSourceWrapper;
    private DBRunner runner = null;
    private MappingGlobalWrapper mappingGlobalWrapper;

    protected void registerColumnType(KeyColumnType type, String typeName) {
        this.columnTypes.put(type, new ColumnType(type, typeName, -1, -1));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, int length) {
        this.columnTypes.put(type, new ColumnType(type, typeName, length, -1));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, int length, int scale) {
        this.columnTypes.put(type, new ColumnType(type, typeName, length, scale));
    }

    public void setDataSourceWrapper(DataSourceWrapper dswrapper) {
        this.dataSourceWrapper = dswrapper;
        this.runner = new DefaultDBRunner(dswrapper);
    }

    public void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
    }

    public List<TableStructure> getTableStructures() throws SQLException {
        StructureBuilder structureBuilder = new StructureBuilder();
        String schema = this.getCatalogAndSchema();
        StampAction table = structureBuilder.table(schema).compile();
        Object result = this.runner(table);
        
        return null;
    }

    public Object runner(StampAction action) throws SQLException {
        StampCombineBuilder combineBuilder = PlatformFactory
                .getStampAlterBuilder(this.dataSourceWrapper.getDatabaseTypeEnum(), action);
        SQLBuilderCombine builderCombine = combineBuilder.getSqlBuilder(this.mappingGlobalWrapper, action);
        return this.runner(new JDBCTraversing(builderCombine.getSql(), builderCombine.getPlaceholders()));
    }

    public Object runner(JDBCTraversing traversing) throws SQLException {
        if (this.runner != null) {
            return this.runner.doHandler(traversing);
        }
        return null;
    }

    /**
     * 供应商          Catalog支持                        Schema支持
     * Oracle         不支持                              Oracle User ID
     * MySQL          不支持                              数据库名
     * MS SQL Server  数据库名                            对象属主名，2005版开始有变
     * DB2            指定数据库对象时，Catalog部分省略      Catalog属主名
     * Sybase         数据库名                            数据库属主名
     * Informix       不支持                              不需要
     * PointBase      不支持                              数据库名
     *
     * @return
     */
    public String getCatalogAndSchema() throws SQLException {
        Connection connection = null;
        try {
            connection = dataSourceWrapper.getConnection();
            String catalog = connection.getCatalog();
            String schema = connection.getSchema();
            return (StringTools.isNotEmpty(catalog) ? catalog + "." : "") + schema;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public ColumnType getColumnType(KeyColumnType type) {
        return columnTypes.get(type);
    }
}
