package org.mimosaframework.orm.mapping;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.platform.DataSourceWrapper;

public class CompareMappingFactory {

    public static StartCompareMapping getCompareMapping(MappingLevel level,
                                                        DataSourceWrapper dataSourceWrapper) {


        if (level == MappingLevel.NOTHING || level == null) {
            return new NothingCompareMapping(dataSourceWrapper);
        }
        if (level == MappingLevel.CREATE) {
            return new AddCompareMapping(dataSourceWrapper);
        }
        if (level == MappingLevel.UPDATE) {
            return new UpdateCompareMapping(dataSourceWrapper);
        }
        if (level == MappingLevel.WARN) {
            return new WarnCompareMapping(dataSourceWrapper);
        }
        throw new IllegalArgumentException(I18n.print("not_support_level"));
    }
}
