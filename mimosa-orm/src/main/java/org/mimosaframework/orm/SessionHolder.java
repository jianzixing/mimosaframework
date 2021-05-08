package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;

/**
 * 在每次数据库操作时创建，之后立刻关闭，数据库链接是否关闭是由
 * 每个实现类来管理的，包括能判断当前的容器(比如spring容器)管理
 * 的事务状态
 */
public interface SessionHolder {
    Session getSession(SessionFactory factory) throws MimosaException;

    boolean begin();

    boolean isSessionTransactional(Session session);

    boolean end();

    boolean close();
}
