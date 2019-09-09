package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.util.Set;

public class CompareMappingFactory {

    public static StartCompareMapping getCompareMapping(MappingLevel level,
                                                        Set<Class> classes,
                                                        ActionDataSourceWrapper dataSourceWrapper,
                                                        MappingNamedConvert convert) {
        if (level == MappingLevel.NOTHING || level == null) {
            return new NothingCompareMapping(classes, dataSourceWrapper, convert);
        }
        if (level == MappingLevel.CREATE) {
            return new AddCompareMapping(classes, dataSourceWrapper, convert);
        }
        if (level == MappingLevel.UPDATE) {
            return new UpdateCompareMapping(classes, dataSourceWrapper, convert);
        }
        throw new IllegalArgumentException("不支持的数据库映射级别");
    }
}
