package org.mimosaframework.orm.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IndexItem {
    String indexName();

    String[] columns();

    boolean unique() default false;
}
