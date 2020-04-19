package org.mimosaframework.orm.platform.sqlite.analysis;

import java.util.List;

public class AnalysisItem {
    public AnalysisType type;
    public String text;
    public List<AnalysisItem> child;

    public AnalysisItem() {
    }

    public AnalysisItem(List<AnalysisItem> child) {
        this.child = child;
        this.type = AnalysisType.CHILD;
    }

    public AnalysisItem(String text) {
        this.text = text;
    }
}
