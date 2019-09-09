package org.mimosaframework.orm.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IndexItem {
    String indexName() default "";

    String[] columns() default {};

    boolean unique() default false;
}
