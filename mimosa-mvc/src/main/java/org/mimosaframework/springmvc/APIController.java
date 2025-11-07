package org.mimosaframework.springmvc;

import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface APIController {

    /**
     * 当前API名称的别名
     * 使用别名后请求的名称也需要换成别名
     *
     * @return
     */
    String value() default "";

    /**
     * URL前缀
     *
     * @return
     */
    String prefix() default "";
}
