package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.List;

public class UpdateCompareMapping extends AddCompareMapping {

    public UpdateCompareMapping(ActionDataSourceWrapper dataSourceWrapper, NotMatchObject notMatchObject) {
        super(dataSourceWrapper, notMatchObject);
    }

    @Override
    public void doMapping() throws SQLException {
        super.doMapping();

        ActionDataSourceWrapper wrapper = dataSourceWrapper.newDataSourceWrapper();
        wrapper.setAutoCloseConnection(true);
        DatabasePorter porter = PlatformFactory.getDatabasePorter(wrapper.getDataSource());
        CarryHandler carryHandler = PlatformFactory.getCarryHandler(wrapper);

        List<MappingField> changeFields = notMatchObject.getChangeFields();

        if (changeFields != null) {
            for (MappingField field : changeFields) {
                // 这里必须保证MappingField的mappingTable存在
                PorterStructure[] structures = porter.updateField(field);
                // 使用SQL语句添加字段
                if (structures != null) {
                    carryHandler.doHandler(structures);
                }
            }
        }
    }
}