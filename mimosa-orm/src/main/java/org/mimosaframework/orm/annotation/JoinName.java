package org.mimosaframework.orm.annotation;

import java.lang.annotation.*;

/**
 * @author yangankang
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinName {
    String value() default "";
}
