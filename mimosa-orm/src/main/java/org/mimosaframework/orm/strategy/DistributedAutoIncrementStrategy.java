package org.mimosaframework.orm.strategy;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;
import org.mimosaframework.orm.platform.PlatformFactory;
import org.mimosaframework.orm.platform.SimpleTemplate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DistributedAutoIncrementStrategy implements IDStrategy {
    private static final Map<String, Boolean> isRunCreate = new ConcurrentHashMap<>();

    @Override
    public Serializable get(StrategyWrapper sw, Session session) throws StrategyException {
        NormalContextContainer values = sw.getValues();
        Map<String, StrategyConfig> map = values.getStrategyDataSource();
        if (map != null) {
            String tableName = sw.getDbTableName();
            String field = sw.getDbField();

            if (sw.getTableClass() == null) {
                throw new IllegalArgumentException("没有传入表的映射类");
            }

            StrategyConfig config = map.get(sw.getTableClass().getName());
            if (config == null) config = map.get(sw.getTableClass().getName() + "#" + field);
            if (config == null) {
                throw new IllegalArgumentException("没有找到分表(表:" + tableName + ",字段:" + field + ")自增ID策略配置");
            }
            MimosaDataSource dataSource = config.getDataSource();
            if (dataSource == null) {
                throw new IllegalArgumentException("分表数据库自增策略没有配置数据源");
            }
            ActionDataSourceWrapper wrapper = sw.getNewDataSourceWrapper();
            wrapper.setAutoCloseConnection(true);
            wrapper.setMaster(true);
            // 如果全局ID表不在主表中
            wrapper.setDataSource(dataSource);

            String name = "_global_" + tableName + "_" + field;
            if (StringTools.isNotEmpty(config.getTableName())) {
                name = config.getTableName();
            }

            SimpleTemplate template = PlatformFactory.getPlatformSimpleSession(wrapper);
            if (isRunCreate.get(name) == null) {
                MappingGlobalWrapper mappingGlobalWrapper = values.getMappingGlobalWrapper();
                MappingTable dbExistTable = mappingGlobalWrapper.getDatabaseTable(dataSource, name);
                if (dbExistTable == null) {
                    DynamicTable table = new DynamicTable();
                    table.setTableName(name);
                    DynamicTableItem item = DynamicTableItem.build("id", long.class, 0, false, "自增列", null);
                    item.setStrategy(AutoIncrementStrategy.class);
                    item.setPk(true);
                    table.addItem(item);
                    table.addItem(DynamicTableItem.build("v", byte.class, 0, false, "辅助的字段", "0"));

                    try {
                        template.createTable(table);
                    } catch (SQLException e) {
                        throw new IllegalStateException("创建数据库自增ID策略表" + name + "失败", e);
                    }
                }
                isRunCreate.put(name, true);
            }

            ModelObject object = new ModelObject();
            object.put("v", 0);
            long id = 0;
            try {
                id = template.save(name, object);
            } catch (SQLException e) {
                throw new IllegalStateException("从表" + name + "获取数据库自增ID策略值失败", e);
            }
            return id;
        }
        return null;
    }
}
