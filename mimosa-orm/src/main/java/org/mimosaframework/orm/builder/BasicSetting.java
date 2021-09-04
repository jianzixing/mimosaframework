package org.mimosaframework.orm.builder;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.convert.NamingConvert;

public class BasicSetting {
    private boolean isShowSQL;
    private NamingConvert convert;
    private String tablePrefix;
    private MappingLevel mappingLevel;
    private Boolean isIgnoreEmptySlave = true;
    private Boolean allowInnerJoin = true;
    private Boolean uppercase = false;

    public boolean isShowSQL() {
        return isShowSQL;
    }

    public void setShowSQL(boolean showSQL) {
        isShowSQL = showSQL;
    }

    public NamingConvert getConvert() {
        return convert;
    }

    public void setConvert(NamingConvert convert) {
        this.convert = convert;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
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

    public Boolean getAllowInnerJoin() {
        return allowInnerJoin;
    }

    public void setAllowInnerJoin(Boolean allowInnerJoin) {
        this.allowInnerJoin = allowInnerJoin;
    }

    public Boolean getUppercase() {
        return uppercase;
    }

    public void setUppercase(Boolean uppercase) {
        this.uppercase = uppercase;
    }
}
