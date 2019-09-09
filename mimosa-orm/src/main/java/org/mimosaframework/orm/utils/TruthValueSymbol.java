package org.mimosaframework.orm.utils;

public interface TruthValueSymbol {
    boolean is(Object source, Object target, String symbol);
}
