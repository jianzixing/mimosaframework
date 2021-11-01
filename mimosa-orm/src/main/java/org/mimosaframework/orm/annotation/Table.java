package org.mimosaframework.orm.annotation;

import java.lang.annotation.*;

/**
 * @author yangankang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value() default "";

    String engineName() default "";

    String charset() default "";

    String version() default "0";

    boolean ignoreSuperclass() default false;
}
