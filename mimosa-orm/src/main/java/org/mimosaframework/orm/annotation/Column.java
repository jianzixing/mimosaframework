package org.mimosaframework.orm.annotation;

import org.mimosaframework.orm.IDStrategy;

import java.lang.annotation.*;

/**
 * @author yangankang
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    Class<? extends IDStrategy> strategy() default IDStrategy.class;

    String name() default "";

    /**
     * {@link Integer} or int.class
     * {@link String}
     * {@link Character} or char.class
     * {@link java.sql.Blob}
     * {@link org.mimosaframework.orm.platform.Text}
     * {@link Byte} or byte.class
     * {@link Short} or short.class
     * {@link Long} or long.class
     * {@link Float} or float.class
     * {@link Double} or double.class
     * {@link java.math.BigDecimal}
     * {@link Boolean} or boolean.class
     * {@link java.sql.Date}
     * {@link java.sql.Time}
     * {@link java.util.Date}
     * {@link java.sql.Timestamp}
     *
     * @return
     */
    Class<?> type() default String.class;

    int length() default 255;

    /**
     * 字段限制和length是一样的效果
     * 但是length只能指定字段长度，这个限定更广泛
     * 比如 Mysql的BigDecimal类型可以传入 32,2
     *
     * @return
     */
    int decimalDigits() default 0;

    boolean nullable() default true;

    /**
     * 当前字段是否是主键
     * 如果一个映射类有多个pk则作为符合主键
     *
     * @return
     */
    boolean pk() default false;

    /**
     * 是否在创建表时创建索引
     *
     * @return
     */
    boolean index() default false;

    /**
     * 是否全局唯一
     * 只支持单表,在分表情况下使用也只是某个单表有效
     *
     * @return
     */
    boolean unique() default false;

    String comment() default "";

    /**
     * 创建一个时间类型字段,在任何更新添加时都会更新时间,示例SQL如下:
     * `modified_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
     *
     * @return
     */
    boolean timeForUpdate() default false;

    String defaultValue() default "";

    /**
     * 扩展注解和数据库无关
     * 用于校验数据最小长度
     *
     * @return
     */
    long extMinLength() default -1;

    /**
     * 扩展注解和数据库无关
     * 用于改变浮点数字的格式，比如
     * .00 保留两位小数
     *
     * @return
     */
    String extDecimalFormat() default "";

    /**
     * 扩展注解和数据库无关
     * 用于校验字符串是否符合正则表达式
     *
     * @return
     */
    String extRegExp() default "";

    /**
     * 扩展注解和数据库无关
     * 指定字段是否允许参与更新操作
     * 只在 {@link org.mimosaframework.orm.Session} 中 方法updateObject有效
     *
     * @return
     */
    boolean extCanUpdate() default true;
}
