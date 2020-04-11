package org.mimosaframework.orm.mapping;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.platform.DataSourceWrapper;

public class CompareMappingFactory {

    public static StartCompareMapping getCompareMapping(MappingLevel level,
                                                        DataSourceWrapper dataSourceWrapper,
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
        throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                CompareMappingFactory.class, "not_support_level"));
    }
}
