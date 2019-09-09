package org.mimosaframework.orm.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Index {
    IndexItem[] value() default {};
}
