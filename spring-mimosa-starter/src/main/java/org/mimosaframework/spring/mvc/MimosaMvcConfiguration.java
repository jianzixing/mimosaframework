package org.mimosaframework.spring.mvc;

import org.mimosaframework.orm.spring.SpringMimosaSessionTemplate;
import org.mimosaframework.springmvc.MimosaHandlerMethodExceptionResolver;
import org.mimosaframework.springmvc.MimosaRequestHandlerAdapter;
import org.mimosaframework.springmvc.MimosaRequestHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用方法，直接在SpringBootApplication的同级目录下编写类
 * <p>
 * \@Configuration
 * public class MimosaAPIConfig extends MimosaMvcConfiguration {
 * }
 * <p>
 * 即可
 */
@Configuration
@ConditionalOnClass({RequestMappingHandlerMapping.class, RequestMappingHandlerAdapter.class})
@ConditionalOnBean()
@EnableConfigurationProperties({MimosaMvcProperties.class})
public class MimosaMvcConfiguration implements WebMvcRegistrations {

    @Autowired
    protected MimosaMvcProperties mimosaMvcProperties;

    @Autowired(required = false)
    protected SpringMimosaSessionTemplate mimosaSessionTemplate;

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        MimosaRequestHandlerMapping requestHandlerMapping = new MimosaRequestHandlerMapping();
        requestHandlerMapping.setPrefix(mimosaMvcProperties.getPrefix());
        requestHandlerMapping.setModule(mimosaMvcProperties.getModule());
        requestHandlerMapping.setSessionTemplate(mimosaSessionTemplate);
        requestHandlerMapping.setReplaces(mimosaMvcProperties.getReplaces());
        return requestHandlerMapping;
    }

    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        MimosaRequestHandlerAdapter requestMappingHandlerAdapter = new MimosaRequestHandlerAdapter();
        requestMappingHandlerAdapter.setBeforeArgumentResolvers(this.getArgsResolver());
        requestMappingHandlerAdapter.setBeforeReturnValueHandlers(this.getReturnHandler());
        return requestMappingHandlerAdapter;
    }

    protected List<HandlerMethodReturnValueHandler> getReturnHandler() {
        return null;
    }

    protected List<HandlerMethodArgumentResolver> getArgsResolver() {
        return null;
    }


    @Override
    public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        MimosaHandlerMethodExceptionResolver resolver = new MimosaHandlerMethodExceptionResolver();
        return resolver;
    }
}
