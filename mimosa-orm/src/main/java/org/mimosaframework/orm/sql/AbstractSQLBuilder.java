package org.mimosaframework.orm.sql;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSQLBuilder {
    protected List gammars = new ArrayList<>();

    protected boolean previous(Object is) {
        if (gammars.size() > 1) {
            Object p = gammars.get(gammars.size() - 2);
            if (p != null && p.equals(is)) return true;
        }
        return false;
    }

    protected boolean hasPrevious(Object... keyword) {
        for (int j = 0; j < keyword.length; j++) {
            for (int i = gammars.size() - 1; i >= 0; i--) {
                Object k = gammars.get(i);
                if (k.equals(keyword)) return true;
            }
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

    protected boolean hasPreviousStops(Object[] keyword, Object[] stops) {
        for (int j = 0; j < keyword.length; j++) {
            for (int i = gammars.size() - 1; i >= 0; i--) {
                Object k = gammars.get(i);
                if (k.equals(keyword)) return true;
                boolean has = false;
                for (Object stop : stops) {
                    if (stop.equals(k)) {
                        has = true;
                        break;
                    }
                }
                if (has) break;
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
                    if ((i + j) < gammars.size() && gammars.get(i + j).equals(s)) {
                    } else {
                        return false;
                    }
                    j++;
                }
                return true;
            }
        }
        return false;
    }

    protected int indexOf(Object keyword) {
        return this.gammars.lastIndexOf(keyword);
    }
}
