package org.mimosaframework.orm.builder;


public class ApplicationSetting {
    private String applicationName;
    private String applicationDetail;

    public ApplicationSetting(String applicationName, String applicationDetail) {
        this.applicationName = applicationName;
        this.applicationDetail = applicationDetail;
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
}
