package org.mimosaframework.orm.sql;

public interface CommentBuilder<T> extends UnifyBuilder{
    T comment(String comment);
}
