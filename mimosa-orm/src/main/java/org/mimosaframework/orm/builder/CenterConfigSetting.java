package org.mimosaframework.orm.builder;

import org.mimosaframework.core.utils.StringTools;

public class CenterConfigSetting {
    private String centerHost;
    private int centerPort;
    private String clientName;

    public String getCenterHost() {
        return centerHost;
    }

    public void setCenterHost(String centerHost) {
        this.centerHost = centerHost;
    }

    public int getCenterPort() {
        return centerPort;
    }

    public void setCenterPort(int centerPort) {
        this.centerPort = centerPort;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean valid() {
        if (StringTools.isNotEmpty(centerHost)
                && centerPort != 0
                && StringTools.isNotEmpty(clientName)) {
            return true;
        }
        return false;
    }
}
