package org.mimosaframework.orm.strategy;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.Configuration;
import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.Session;
import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StrategyFactory {
    private static final Map<String, IDStrategy> strategys = new ConcurrentHashMap<>();

    public static Serializable applyStrategy(Configuration values,
                                             MappingTable table,
                                             ModelObject object,
                                             ModelObject source,
                                             Session session) throws StrategyException {
        return applyStrategy(values, table, object, source, session, null);
    }

    public static Serializable applyStrategy(Configuration values,
                                             MappingTable table,
                                             ModelObject object,
                                             Session session) throws StrategyException {
        return applyStrategy(values, table, object, null, session, null);
    }

    /**
     * MappingTable 是Class的映射类，不包含数据库字段
     *
     * @param values
     * @param table
     * @param object
     */
    public static Serializable applyStrategy(Configuration values,
                                             MappingTable table,
                                             ModelObject object,
                                             ModelObject source,
                                             Session session,
                                             Class<? extends IDStrategy> defaultAutoIncr) throws StrategyException {
        Set<MappingField> fields = table.getMappingFields();
        Class mappingClass = table.getMappingClass();
        for (MappingField f : fields) {
            String key = mappingClass.getName() + "_" + f.getMappingFieldName();
            Column column = f.getMappingFieldAnnotation();
            if (column != null) {
                // 如果表没有分表则使用数据库默认规则
                Class<? extends IDStrategy> c = column.strategy();

                // 如果传入默认策略使用默认的数据库ID自增策略
                if (c == AutoIncrementStrategy.class
                        && defaultAutoIncr != null) {
                    c = defaultAutoIncr;
                }

                if (c != AutoIncrementStrategy.class && c != IDStrategy.class) {
                    if (strategys.get(c) == null) {
                        // 假如有已经实例好的ID生成策略对象，就用已经生成好的对象
                        List<? extends IDStrategy> list = values.getIdStrategies();
                        if (list != null && list.size() > 0) {
                            for (IDStrategy strategy : list) {
                                if (strategy.getClass().isAssignableFrom(c)) {
                                    strategys.put(key, strategy);
                                }
                            }
                        }

                        if (strategys.get(key) == null) {
                            try {
                                strategys.put(key, c.newInstance());
                            } catch (Exception e) {
                                throw new StrategyException(I18n.print("create_strategy_error"), e);
                            }
                        }
                    }
                    IDStrategy strategy = strategys.get(key);
                    StrategyWrapper wrapper = new StrategyWrapper(values);
                    wrapper.setTableName(table.getMappingTableName());
                    wrapper.setDbTableName(table.getMappingTableName());
                    wrapper.setField(f.getMappingFieldName());
                    wrapper.setDbField(f.getMappingColumnName());
                    wrapper.setTableClass(table.getMappingClass());
                    Serializable serializable = strategy.get(wrapper, session);
                    if (object != null) object.put(f.getMappingFieldName(), serializable);
                    if (source != null) source.put(f.getMappingFieldName(), serializable);
                    return serializable;
                }
            }
        }
        return null;
    }
}
