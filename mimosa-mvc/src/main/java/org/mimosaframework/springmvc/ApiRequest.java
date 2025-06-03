package org.mimosaframework.springmvc;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiRequest {
    String name() default "";

    String value() default "";

    String code() default "";

    RequestMethod[] method() default {};

    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};

    boolean plaintext() default true;

    String contentType() default "";

    // 接口作用分类，比如后台的，前端的，自定义使用方式
    ApiAction action() default ApiAction.DEFAULT;

    // 接口级别分类
    ApiLevel level() default ApiLevel.LOGIN;

    // 接口作用作用，比如用作查询的，用作删除的等等，自定义使用方式
    String type() default "";

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
