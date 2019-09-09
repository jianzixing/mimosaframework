package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.platform.mysql.MysqlDatabasePorter;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddCompareMapping extends NothingCompareMapping {
    public AddCompareMapping(Set<Class> mappingClasses, ActionDataSourceWrapper dataSourceWrapper, MappingNamedConvert convert) {
        super(mappingClasses, dataSourceWrapper, convert);
    }

    @Override
    public Map<MimosaDataSource, NotMatchObject> doMapping() throws SQLException {
        Map<MimosaDataSource, NotMatchObject> map = super.doMapping();
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<MimosaDataSource, NotMatchObject>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<MimosaDataSource, NotMatchObject> next = iterator.next();

                MimosaDataSource ds = next.getKey();
                NotMatchObject nmo = next.getValue();
                ActionDataSourceWrapper dswrapper = dataSourceWrapper.newDataSourceWrapper();
                dswrapper.setAutoCloseConnection(true);
                dswrapper.setDataSource(ds);
                DatabasePorter porter = PlatformFactory.getDatabasePorter(ds);
                CarryHandler carryHandler = PlatformFactory.getCarryHandler(dswrapper);

                List<MappingTable> missingTables = nmo.getMissingTables();
                List<MappingField> missingFields = nmo.getMissingFields();

                if (missingTables != null) {
                    for (MappingTable table : missingTables) {
                        String tableName = table.getDatabaseTableName();
                        String tableClassName = table.getMappingClass().getSimpleName();
                        PorterStructure[] structures = porter.createTable(table);
                        // 使用SQL语句添加表
                        if (structures != null) {
                            try {
                                carryHandler.doHandler(structures);
                            } catch (SQLException e) {
                                throw new SQLException(
                                        "向数据库添加新表" +
                                                "[" + tableName + "]出错," +
                                                "请检查映射类[" + tableClassName + "]" +
                                                ",如果出现不支持情况请手动建表", e);
                            }
                            isUpdateDatabaseTable = true;
                        }
                    }
                }

                if (missingFields != null) {
                    for (MappingField field : missingFields) {
                        // 这里必须保证MappingField的mappingTable存在
                        MappingTable table = field.getMappingTable();
                        String tableName = table.getDatabaseTableName();
                        String tableClassName = table.getMappingClass().getSimpleName();
                        String fieldName = field.getMappingFieldName();
                        PorterStructure[] structures = porter.createField(field);
                        // 使用SQL语句添加字段
                        if (structures != null) {
                            try {
                                carryHandler.doHandler(structures);
                            } catch (SQLException e) {
                                throw new SQLException(
                                        "向数据库表[" + tableName + "]" +
                                                "添加新字段[" + fieldName + "]出错,请检查映射类[" + tableClassName + "]," +
                                                "请手动修改数据库表字段信息", e);
                            }
                            isUpdateDatabaseTable = true;
                        }
                    }
                }
            }
        }
        return map;
    }
}
