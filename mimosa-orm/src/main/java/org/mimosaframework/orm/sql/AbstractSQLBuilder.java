package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.SQLAutonomously;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSQLBuilder implements UnifyBuilder {
    protected List gammars = new ArrayList<>();
    protected List points = null;
    protected Object point = null;
    protected int posPoint = -1;

    private SQLAutonomously autonomously;

    public void setAutonomously(SQLAutonomously autonomously) {
        this.autonomously = autonomously;
    }

    @Override
    public SQLAutonomously autonomously() {
        return this.autonomously;
    }

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

    protected boolean hasPreviousStop(Object... keyword) {
        Object stop = keyword[keyword.length - 1];
        for (int j = 0; j < keyword.length - 1; j++) {
            for (int i = gammars.size() - 1; i >= 0; i--) {
                Object k = gammars.get(i);
                if (k.equals(keyword[j])) return true;
                if (stop.equals(k)) break;
            }
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

    protected boolean isAfter(Object keyword, int start, Object... is) {
        for (int i = gammars.size() - 1; i >= 0; i--) {
            if (i <= start) break;
            Object k = gammars.get(i);
            if (k.equals(keyword)) {
                int j = 1;
                for (Object s : is) {
                    if (!s.equals("*")) {
                        if ((i + j) < gammars.size() && gammars.get(i + j).equals(s)) {
                        } else {
                            return false;
                        }
                    }
                    j++;
                }
                return true;
            }
        }
        return false;
    }

    protected boolean has(Object keyword) {
        for (int i = 0; i < gammars.size(); i++) {
            if (gammars.get(i).equals(keyword)) return true;
        }
        return false;
    }

    protected int indexOf(Object keyword) {
        return this.gammars.lastIndexOf(keyword);
    }
}
