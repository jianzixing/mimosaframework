package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.util.Set;

public class CompareMappingFactory {

    public static StartCompareMapping getCompareMapping(MappingLevel level,
                                                        ActionDataSourceWrapper dataSourceWrapper,
                                                        Set<MappingTable> mappingTables) {
        if (level == MappingLevel.NOTHING || level == null) {
            return new NothingCompareMapping(dataSourceWrapper, mappingTables);
        }
        if (level == MappingLevel.CREATE) {
            return new AddCompareMapping(dataSourceWrapper, mappingTables);
        }
        if (level == MappingLevel.UPDATE) {
            return new UpdateCompareMapping(dataSourceWrapper, mappingTables);
        }
        if (level == MappingLevel.WARN) {
            return new WarnCompareMapping(dataSourceWrapper, mappingTables);
        }
        throw new IllegalArgumentException("不支持的数据库映射级别");
    }
}
