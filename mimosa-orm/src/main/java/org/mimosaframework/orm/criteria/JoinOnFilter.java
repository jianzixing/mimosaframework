package org.mimosaframework.orm.criteria;

public class JoinOnFilter {
    private Filter filter;
    private OnField onField;
    private boolean isOn;

    public JoinOnFilter(Filter filter) {
        this.filter = filter;
        this.isOn = false;
    }

    public JoinOnFilter(OnField onField) {
        this.onField = onField;
        this.isOn = true;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public OnField getOnField() {
        return onField;
    }

    public void setOnField(OnField onField) {
        this.onField = onField;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
