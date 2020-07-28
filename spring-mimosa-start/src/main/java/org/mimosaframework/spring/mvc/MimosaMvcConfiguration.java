package org.mimosaframework.spring.mvc;

import org.mimosaframework.orm.MimosaSessionTemplate;
import org.mimosaframework.springmvc.MimosaHandlerMethodExceptionResolver;
import org.mimosaframework.springmvc.MimosaRequestHandlerAdapter;
import org.mimosaframework.springmvc.MimosaRequestHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ConditionalOnClass({RequestMappingHandlerMapping.class, RequestMappingHandlerAdapter.class})
@EnableConfigurationProperties({MimosaMvcProperties.class})
public class MimosaMvcConfiguration implements WebMvcRegistrations {

    @Autowired
    MimosaMvcProperties mimosaMvcProperties;

    @Autowired
    private MimosaSessionTemplate mimosaSessionTemplate;

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        MimosaRequestHandlerMapping requestHandlerMapping = new MimosaRequestHandlerMapping();
        requestHandlerMapping.setSessionTemplate(mimosaSessionTemplate);
        requestHandlerMapping.setCurdImplementClass(mimosaMvcProperties.getCurdImplementClass());
        requestHandlerMapping.setPrefixs(mimosaMvcProperties.getPrefixs());
        requestHandlerMapping.setReplaces(mimosaMvcProperties.getReplaces());
        return requestHandlerMapping;
    }

    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        MimosaRequestHandlerAdapter requestMappingHandlerAdapter = new MimosaRequestHandlerAdapter();
        return requestMappingHandlerAdapter;
    }

    @Override
    public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        return new MimosaHandlerMethodExceptionResolver();
    }
}
