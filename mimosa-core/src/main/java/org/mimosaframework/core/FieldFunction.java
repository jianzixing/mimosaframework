package org.mimosaframework.core;

import java.io.Serializable;

//该注解表示这是一个函数式接口：即接口中有且只能一个函数声明。
//注意：一定要继承序列化`Serializable`接口，后面会分析原因。
@FunctionalInterface
public interface FieldFunction<T> extends Serializable {
    Object apply(T source);
}