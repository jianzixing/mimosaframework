package org.mimosaframework.orm.sql;

import org.mimosaframework.core.utils.StringTools;

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

    protected boolean hasPrevious(Object keyword, String stop) {
        return hasPreviousStop(new Object[]{keyword, stop});
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
                if (k.equals(keyword)) return true;
                if (stop.equals(k)) break;
            }
        }
        return false;
    }
}
