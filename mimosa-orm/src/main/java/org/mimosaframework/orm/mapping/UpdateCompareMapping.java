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

public class UpdateCompareMapping extends AddCompareMapping {

    public UpdateCompareMapping(Set<Class> mappingClasses, ActionDataSourceWrapper dataSourceWrapper, MappingNamedConvert convert) {
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

                List<MappingField> changeFields = nmo.getChangeFields();

                if (changeFields != null) {
                    for (MappingField field : changeFields) {
                        // 这里必须保证MappingField的mappingTable存在
                        PorterStructure[] structures = porter.updateField(field);
                        // 使用SQL语句添加字段
                        if (structures != null) {
                            carryHandler.doHandler(structures);
                            isUpdateDatabaseTable = true;
                        }
                    }
                }
            }
        }
        return map;
    }
}
