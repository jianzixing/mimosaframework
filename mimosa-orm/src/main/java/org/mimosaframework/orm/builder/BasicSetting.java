package org.mimosaframework.orm.builder;

import org.mimosaframework.orm.AbstractInterceptSession;
import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.convert.MappingNamedConvert;

public class BasicSetting {
    private boolean isShowSQL;
    private MappingNamedConvert convert;
    private MappingLevel mappingLevel;
    private Boolean isIgnoreEmptySlave = true;
    private AbstractInterceptSession interceptSession;

    public boolean isShowSQL() {
        return isShowSQL;
    }

    public void setShowSQL(boolean showSQL) {
        isShowSQL = showSQL;
    }

    public MappingNamedConvert getConvert() {
        return convert;
    }

    public void setConvert(MappingNamedConvert convert) {
        this.convert = convert;
    }

    public void setMappingLevel(MappingLevel mappingLevel) {
        this.mappingLevel = mappingLevel;
    }

    public MappingLevel getMappingLevel() {
        return mappingLevel;
    }

    public Boolean isIgnoreEmptySlave() {
        return isIgnoreEmptySlave;
    }

    public void setIgnoreEmptySlave(Boolean ignoreEmptySlave) {
        isIgnoreEmptySlave = ignoreEmptySlave;
    }

    public AbstractInterceptSession getInterceptSession() {
        return interceptSession;
    }

    public void setInterceptSession(AbstractInterceptSession interceptSession) {
        this.interceptSession = interceptSession;
    }
}
