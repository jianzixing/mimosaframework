package org.mimosaframework.orm.platform.sqlite.analysis;

import org.mimosaframework.core.utils.StringTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLAnalysis {
    private int bracket = 0;
    private int mark = 0;
    private Map<Integer, List<AnalysisItem>> childMap = new HashMap<>();
    private StringBuilder sb = new StringBuilder();

    public List<AnalysisItem> analysis(String sql) {
        char[] arr = sql.toCharArray();
        List<AnalysisItem> items = new ArrayList<>();
        this.analysis(items, arr, new IndexHolder());
        return items;
    }

    private List<AnalysisItem> analysis(List<AnalysisItem> items, char[] arr, IndexHolder holder) {
        childMap.put(0, items);
        for (; holder.index < arr.length; holder.index++) {
            char c = arr[holder.index];
            if (c == ' ' || c == '\n' || c == '\t') {
                this.addText();
            } else if (c == '(') {
                this.addText();
                List<AnalysisItem> parent = childMap.get(bracket);
                bracket++;
                List<AnalysisItem> children = new ArrayList<>();
                parent.add(new AnalysisItem(children));
                childMap.put(bracket, children);
            } else if (c == ')') {
                this.addText();
                bracket--;
            } else if (c == '"') {
                mark++;
                if (mark == 2) {
                    this.addText();
                    mark = 0;
                } else if (mark == 1) {
                    this.addText();
                }
            } else if (c == ',') {
                this.addText();
                sb.append(c);
                this.addText();
            } else {
                sb.append(c);
            }
        }
        return items;
    }

    private void addText() {
        String text = sb.toString();
        if (StringTools.isNotEmpty(text)) {
            List<AnalysisItem> children = childMap.get(bracket);
            if (children != null) {
                children.add(new AnalysisItem(text));
            }
        }
        sb.setLength(0);
    }
}
