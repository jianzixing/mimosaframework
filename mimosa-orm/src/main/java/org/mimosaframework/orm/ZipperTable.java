package org.mimosaframework.orm;

import java.io.Closeable;

/**
 * 拉链表实现
 * 可以将某个表数据从开头遍历到结尾
 */
public interface ZipperTable<T> extends Iterable<T>, Closeable {

    /**
     * Mysql上,这个值设置必须得URL的支持
     *
     * @param size
     */
    void setFetchSize(int size);
}
