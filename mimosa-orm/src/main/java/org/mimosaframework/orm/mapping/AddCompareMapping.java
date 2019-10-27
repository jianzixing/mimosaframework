package org.mimosaframework.orm.mapping;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
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
                    String tableName = table.getMappingTableName();
                    String tableClassName = table.getMappingClass().getSimpleName();
                    // 使用SQL语句添加表
                    try {
                        porter.createTable(table);
                    } catch (SQLException e) {
                        throw new SQLException(Messages.get(LanguageMessageFactory.PROJECT, AddCompareMapping.class,
                                "add_new_table_error",
                                tableName, tableClassName), e);
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
                        throw new SQLException(Messages.get(LanguageMessageFactory.PROJECT, AddCompareMapping.class,
                                "add_new_field_error",
                                tableName, fieldName, tableClassName), e);
                    }
                }
            }
        }
    }
}
