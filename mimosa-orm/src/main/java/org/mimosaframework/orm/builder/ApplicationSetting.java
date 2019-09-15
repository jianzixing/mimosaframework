package org.mimosaframework.orm.builder;

import org.mimosaframework.orm.IDStrategy;

import java.util.List;

public class ApplicationSetting {
    private String applicationName;
    private String applicationDetail;
    private List<? extends IDStrategy> idStrategies;

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

    public List<? extends IDStrategy> getIdStrategies() {
        return idStrategies;
    }

    public void setIdStrategies(List<? extends IDStrategy> idStrategies) {
        this.idStrategies = idStrategies;
    }
}
