package org.mimosaframework.orm.scripting;

public interface SqlNode {
    boolean apply(DynamicContext context);
}
