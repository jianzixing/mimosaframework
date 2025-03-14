package org.mimosaframework.springmvc;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Body {
    String name() default "";

    String value() default "";

    /**
     * 如果不填写使用的是 类名称+方法名称 方式，如果填写了
     * 那么就按照当前填写的作为路径
     */
    String code() default "";

    RequestMethod[] method() default {};

    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};

    boolean plaintext() default true;

    String contentType() default "";

    // 接口描述
    String detail() default "";

    // 仅给第三方做权限码使用
    String menu() default "";

    // 仅给第三方做权限码使用
    String[] menus() default {};

    // 仅给第三方做权限码使用,资源码
    String resource() default "";

    String[] resources() default "";
}
