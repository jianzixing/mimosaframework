package org.mimosaframework.springmvc;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CURDPrinter {
    String name() default "";

    String pk();
}
