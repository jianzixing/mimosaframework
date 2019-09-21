package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.List;

public class AddCompareMapping extends NothingCompareMapping {
    public AddCompareMapping(ActionDataSourceWrapper dataSourceWrapper, NotMatchObject notMatchObject) {
        super(dataSourceWrapper, notMatchObject);
    }

    @Override
    public void doMapping() throws SQLException {
        super.doMapping();
        if (notMatchObject != null) {
            ActionDataSourceWrapper wrapper = dataSourceWrapper.newDataSourceWrapper();
            wrapper.setAutoCloseConnection(true);

            DatabasePorter porter = PlatformFactory.getDatabasePorter(wrapper);
            CarryHandler carryHandler = PlatformFactory.getCarryHandler(wrapper);

            List<MappingTable> missingTables = notMatchObject.getMissingTables();
            List<MappingField> missingFields = notMatchObject.getMissingFields();

            if (missingTables != null) {
                for (MappingTable table : missingTables) {
                    String tableName = table.getDatabaseTableName();
                    String tableClassName = table.getMappingClass().getSimpleName();
                    // 使用SQL语句添加表
                    try {
                        porter.createTable(table);
                    } catch (SQLException e) {
                        throw new SQLException(
                                "向数据库添加新表" +
                                        "[" + tableName + "]出错," +
                                        "请检查映射类[" + tableClassName + "]" +
                                        ",如果出现不支持情况请手动建表", e);
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
                    // 使用SQL语句添加字段
                    try {
                        porter.createField(field);
                    } catch (SQLException e) {
                        throw new SQLException(
                                "向数据库表[" + tableName + "]" +
                                        "添加新字段[" + fieldName + "]出错,请检查映射类[" + tableClassName + "]," +
                                        "请手动修改数据库表字段信息", e);
                    }
                }
            }
        }
    }
}
