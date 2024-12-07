package org.mimosaframework.springmvc;

import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * 用于标识类是否开放对外访问请求，当前是使用的Spring beans
 * 这个标识继承Spring mvc的Controller注解
 * 当一个类使用这个注解后一般会和{@link Body} 一起使用组成一个 类+方法 的访问方式
 * 比如： http://localhost/admin/{ClassName}/{MethodName}
 * <p>
 * 请求时候默认不区分大小写
 * <p>
 * 一般来讲这样做的方式是为了一个能够统一访问的方式，客户端可以根据约定的url来封装API
 * 比如 JS 客户端：Ajax.{ClassName}.{MethodName}.call(...)
 * <p>
 * 现在只实现了http的方式，以后也可以扩展到其他方式
 *
 * @author yangankang
 * @see Body
 */
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
