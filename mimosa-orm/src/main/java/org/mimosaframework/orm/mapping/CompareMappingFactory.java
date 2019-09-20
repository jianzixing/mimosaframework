package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

public class CompareMappingFactory {

    public static StartCompareMapping getCompareMapping(MappingLevel level,
                                                        ActionDataSourceWrapper dataSourceWrapper,
                                                        NotMatchObject notMatchObject) {


        if (level == MappingLevel.NOTHING || level == null) {
            return new NothingCompareMapping(dataSourceWrapper, notMatchObject);
        }
        if (level == MappingLevel.CREATE) {
            return new AddCompareMapping(dataSourceWrapper, notMatchObject);
        }
        if (level == MappingLevel.UPDATE) {
            return new UpdateCompareMapping(dataSourceWrapper, notMatchObject);
        }
        if (level == MappingLevel.WARN) {
            return new WarnCompareMapping(dataSourceWrapper, notMatchObject);
        }
        throw new IllegalArgumentException("不支持的数据库映射级别");
    }
}
