package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.platform.SessionContext;

public class CompareMappingFactory {

    public static StartCompareMapping getCompareMapping(MappingLevel level,
                                                        MappingGlobalWrapper mappingGlobalWrapper,
                                                        SessionContext dataSourceWrapper) {


        if (level == MappingLevel.NOTHING || level == null) {
            return new NothingCompareMapping(mappingGlobalWrapper, dataSourceWrapper);
        }
        if (level == MappingLevel.CREATE) {
            return new CreateCompareMapping(mappingGlobalWrapper, dataSourceWrapper);
        }
        if (level == MappingLevel.UPDATE) {
            return new UpdateCompareMapping(mappingGlobalWrapper, dataSourceWrapper);
        }
        if (level == MappingLevel.WARN) {
            return new WarnCompareMapping(mappingGlobalWrapper, dataSourceWrapper);
        }
        throw new IllegalArgumentException(I18n.print("not_support_level"));
    }
}
