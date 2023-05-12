package org.mimosaframework.springmvc;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Printer {
    String name() default "";

    String value() default "";

    /**
     * 附加的路径
     *
     * @return
     */
    String path() default "";

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
}
