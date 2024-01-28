package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.SQLAutonomously;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSQLBuilder implements UnifyBuilder {
    protected List gammars = new ArrayList<>();
    protected List points = null;
    protected Object point = null;
    protected int posPoint = -1;

    // public SQLAutonomously autonomously() {
    //     SQLAutonomously autonomously = SQLAutonomously.newInstance(this);
    //     return autonomously;
    // }

    protected void addPoint(Object point) {
        this.gammars.add(point);
        if (this.points == null) this.points = new ArrayList();
        this.points.add(point);
        this.point = point;
        this.posPoint = gammars.size() - 1;
    }

    protected Object getPoint(int i) {
        if (this.points != null) {
            int index = this.points.size() - i - 1;
            if (index >= 0) {
                return this.points.get(index);
            }
        }
        return null;
    }

    protected Object getPointNext(int i) {
        if (this.posPoint + i < this.gammars.size()) {
            return this.gammars.get(this.posPoint + i);
        }
        return null;
    }

    protected Object getPrePoint() {
        return this.getPoint(1);
    }

    protected boolean previous(Object is) {
        if (gammars.size() > 1) {
            Object p = gammars.get(gammars.size() - 2);
            if (p != null && p.equals(is)) return true;
        }
        return false;
    }

    protected Object previous(Object... keywords) {
        for (int i = gammars.size() - 1; i >= 0; i--) {
            Object k = gammars.get(i);
            for (int j = 0; j < keywords.length; j++) {
                if (k.equals(keywords[j])) return keywords[j];
            }
        }
        return null;
    }
}
