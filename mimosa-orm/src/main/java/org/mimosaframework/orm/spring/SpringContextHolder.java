package org.mimosaframework.orm.spring;

import org.springframework.context.ApplicationContext;

public interface SpringContextHolder {
    ApplicationContext getSpringContext();

    Object getBean(String beanName);

    String getBeanName(Object object);
}
