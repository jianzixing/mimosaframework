package org.mimosaframework.orm.builder;


public class ApplicationSetting {
    private String applicationName;
    private String applicationDetail;
    private String applicationCode;
    private String version;

    public ApplicationSetting(String applicationName, String applicationDetail) {
        this.applicationName = applicationName;
        this.applicationDetail = applicationDetail;
    }

    public ApplicationSetting(String applicationName, String applicationCode, String applicationDetail) {
        this.applicationName = applicationName;
        this.applicationDetail = applicationDetail;
        this.applicationCode = applicationCode;
    }

    public ApplicationSetting() {
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDetail() {
        return applicationDetail;
    }

    public void setApplicationDetail(String applicationDetail) {
        this.applicationDetail = applicationDetail;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
