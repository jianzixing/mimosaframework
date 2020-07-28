package org.mimosaframework.spring.orm;

import org.mimosaframework.orm.MappingLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mimosa.orm")
public class MimosaOrmProperties {
    private String applicationName = "mimosa";
    private String scanPackage;
    private String mapper;
    private String convertType = "H2U";
    private MappingLevel mappingLevel = MappingLevel.CREATE;
    private boolean showSQL = false;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
    }

    public MappingLevel getMappingLevel() {
        return mappingLevel;
    }

    public void setMappingLevel(MappingLevel mappingLevel) {
        this.mappingLevel = mappingLevel;
    }

    public boolean isShowSQL() {
        return showSQL;
    }

    public void setShowSQL(boolean showSQL) {
        this.showSQL = showSQL;
    }
}
