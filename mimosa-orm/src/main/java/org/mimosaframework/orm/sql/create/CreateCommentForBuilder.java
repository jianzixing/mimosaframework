package org.mimosaframework.orm.sql.create;

public interface CreateCommentForBuilder<T> {
    T tableComment(String comment);
}
