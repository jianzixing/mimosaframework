package org.mimosaframework.springmvc;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.springmvc.i18n.I18n;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringValueResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MimosaRequestHandlerMapping extends RequestMappingHandlerMapping
        implements EmbeddedValueResolverAware {

    protected boolean useSuffixPatternMatch = true;
    protected boolean useRegisteredSuffixPatternMatch = false;
    protected boolean useTrailingSlashMatch = true;
    protected ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
    protected StringValueResolver embeddedValueResolver;
    protected RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

    protected String prefix = null;
    protected String module = null;
    protected Map<String, String> replaces = null;
    protected SessionTemplate sessionTemplate;

    static {
        I18n.register();
    }

    public void setSessionTemplate(SessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setReplaces(Map<String, String> replaces) {
        this.replaces = replaces;
    }

    public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
        this.useSuffixPatternMatch = useSuffixPatternMatch;
    }

    public void setUseRegisteredSuffixPatternMatch(boolean useRegisteredSuffixPatternMatch) {
        this.useRegisteredSuffixPatternMatch = useRegisteredSuffixPatternMatch;
        this.useSuffixPatternMatch = (useRegisteredSuffixPatternMatch || this.useSuffixPatternMatch);
    }

    public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
        this.useTrailingSlashMatch = useTrailingSlashMatch;
    }

    public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        Assert.notNull(contentNegotiationManager, "ContentNegotiationManager must not be null");
        this.contentNegotiationManager = contentNegotiationManager;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }


    public boolean useSuffixPatternMatch() {
        return this.useSuffixPatternMatch;
    }

    public boolean useRegisteredSuffixPatternMatch() {
        return this.useRegisteredSuffixPatternMatch;
    }

    public boolean useTrailingSlashMatch() {
        return this.useTrailingSlashMatch;
    }

    public ContentNegotiationManager getContentNegotiationManager() {
        return this.contentNegotiationManager;
    }

    public List<String> getFileExtensions() {
        return this.config.getFileExtensions();
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return ((AnnotationUtils.findAnnotation(beanType, Controller.class) != null) ||
                (AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null));
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = createRequestMappingInfo(method);
        if (info != null) {
            RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        ApiRequest apiRequest = AnnotatedElementUtils.findMergedAnnotation(element, ApiRequest.class);
        RequestCondition<?> condition = (element instanceof Class<?> ?
                getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        if (requestMapping != null) {
            return createRequestMappingInfo(requestMapping, condition, element);
        }

        // only support method
        if (element instanceof Method && apiRequest != null) {
            APIController apiController = ((Method) element).getDeclaringClass().getAnnotation(APIController.class);
            if (apiController != null) {
                List<String> list = this.getApiCodes(element, apiController, apiRequest);
                return createRequestMappingInfoByPrinter(apiRequest, condition, list.toArray(new String[]{}));
            }
        }

        return null;
    }

    private List<String> getApiCodes(AnnotatedElement element, APIController apiController, ApiRequest apiRequest) {
        Class<?> type = ((Method) element).getDeclaringClass();
        String className = ((Method) element).getDeclaringClass().getSimpleName();
        String methodName = ((Method) element).getName();
        String prefixUri = this.getCommonPrefix(apiController);

        if (prefixUri.trim().endsWith("/")) {
            prefixUri = prefixUri.substring(0, prefixUri.length() - 1);
        }

        String apiCode = null;
        if (apiRequest != null) apiCode = apiRequest.code();

        if (StringTools.isNotEmpty(apiCode)) {
            return Collections.singletonList(prefixUri + "/" + apiCode);
        }

        if (className.endsWith("Controller")) {
            className = className.substring(0, className.length() - "Controller".length());
        }
        className = this.replaceCommonPrefix(type, className);

        String methodValue = null;
        if (apiRequest != null) methodValue = apiRequest.value();

        String module = StringTools.isNotEmpty(this.module) ? this.module : "system";

        if (StringTools.isNotEmpty(apiController.value())) {
            if (StringTools.isNotEmpty(methodValue)) {
                return Collections.singletonList(prefixUri + "/" + module + "." + apiController.value() + "." + methodValue);
            } else {
                List<String> list = new ArrayList<>();
                list.add(prefixUri + "/" + module + "." + apiController.value() + "." + StringTools.humpToLine(methodName, true));
                list.add(prefixUri + "/" + module + "." + apiController.value() + "." + methodName);
                return list;
            }
        } else {
            List<String> list = new ArrayList<>();
            if (StringTools.isNotEmpty(methodValue)) {
                list.add(prefixUri + "/" + module + "." + StringTools.humpToLine(className, true) + "." + methodValue);
                list.add(prefixUri + "/" + module + "." + className + "." + methodValue);
            } else {
                list.add(prefixUri + "/" + module + "." + StringTools.humpToLine(className, true) + "." + StringTools.humpToLine(methodName, true));
                list.add(prefixUri + "/" + module + "." + className + "." + methodName);
            }
            return list;
        }
    }

    private synchronized String getCommonPrefix(APIController apiController) {
        if (StringTools.isNotEmpty(apiController.prefix())) {
            return apiController.prefix();
        }
        if (prefix != null) {
            return prefix;
        }
        return "";
    }

    private synchronized String replaceCommonPrefix(Class<?> c, String name) {
        String clsName = c.getName();
        String simpleName = c.getSimpleName();
        clsName = clsName.substring(0, clsName.length() - simpleName.length() - 1);
        if (replaces != null) {
            String replace = replaces.get(clsName);
            if (StringTools.isNotEmpty(replace)) {
                String[] rs = replace.split(":");
                if (rs.length >= 1) {
                    String r1 = rs[0].trim();
                    String r2 = rs.length == 1 ? "" : rs[1];
                    if (name.startsWith(rs[0].trim())) {
                        return name.replaceFirst(r1, r2.trim());
                    }
                }
            }
        }
        return name;
    }

    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return null;
    }

    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return null;
    }

    protected RequestMappingInfo createRequestMappingInfoByPrinter(ApiRequest requestMapping, RequestCondition<?> customCondition, String[] path) {
        return RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(path))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name())
                .customCondition(customCondition)
                .options(this.config)
                .build();
    }

    protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, RequestCondition<?> customCondition, AnnotatedElement element) {
        return createRequestMappingInfo(requestMapping, customCondition);
    }

    protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, RequestCondition<?> customCondition) {

        return RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name())
                .customCondition(customCondition)
                .options(this.config)
                .build();
    }

    protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
        if (this.embeddedValueResolver == null) {
            return patterns;
        } else {
            String[] resolvedPatterns = new String[patterns.length];
            for (int i = 0; i < patterns.length; i++) {
                resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
            }
            return resolvedPatterns;
        }
    }
}
