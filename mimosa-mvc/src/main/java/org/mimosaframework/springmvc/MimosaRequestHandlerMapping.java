package org.mimosaframework.springmvc;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.springmvc.i18n.I18n;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

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
        this.config = new RequestMappingInfo.BuilderConfiguration();
        try {
            this.config.getClass().getMethod("setPathHelper", UrlPathHelper.class);
            this.config.setPathHelper(getUrlPathHelper());
        } catch (NoSuchMethodException e) {
            this.config.setUrlPathHelper(getUrlPathHelper());
        }
        this.config.setPathMatcher(getPathMatcher());
        this.config.setSuffixPatternMatch(this.useSuffixPatternMatch);
        this.config.setTrailingSlashMatch(this.useTrailingSlashMatch);
        this.config.setRegisteredSuffixPatternMatch(this.useRegisteredSuffixPatternMatch);
        this.config.setContentNegotiationManager(getContentNegotiationManager());

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
        Body body = AnnotatedElementUtils.findMergedAnnotation(element, Body.class);
        ApiRequest apiRequest = AnnotatedElementUtils.findMergedAnnotation(element, ApiRequest.class);
        RequestCondition<?> condition = (element instanceof Class<?> ?
                getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        if (requestMapping != null) {
            return createRequestMappingInfo(requestMapping, condition, element);
        }

        // only support method
        if (element instanceof Method) {
            APIController apiController = ((Method) element).getDeclaringClass().getAnnotation(APIController.class);
            if (apiController != null) {
                if (body != null) {
                    List<String> list = this.getApiCodes(element, apiController, body, apiRequest);
                    return createRequestMappingInfoByPrinter(body, condition, list.toArray(new String[]{}));
                } else if (apiRequest != null) {
                    List<String> list = this.getApiCodes(element, apiController, body, apiRequest);
                    return createRequestMappingInfoByPrinter(apiRequest, condition, list.toArray(new String[]{}));
                }
            }
        }

        return null;
    }

    private List<String> getApiCodes(AnnotatedElement element, APIController apiController, Body body, ApiRequest apiRequest) {
        Class<?> type = ((Method) element).getDeclaringClass();
        String className = ((Method) element).getDeclaringClass().getSimpleName();
        String methodName = ((Method) element).getName();
        String prefixUri = this.getCommonPrefix(apiController);

        String apiCode = null;
        if (body != null) apiCode = body.code();
        if (apiRequest != null && StringTools.isEmpty(apiCode)) apiCode = apiRequest.code();

        if (StringTools.isNotEmpty(apiCode)) {
            return Collections.singletonList(prefixUri + "/" + apiCode);
        }

        if (className.endsWith("Controller")) {
            className = className.substring(0, className.length() - "Controller".length());
        }
        className = this.replaceCommonPrefix(type, className);

        String methodValue = null;
        if (body != null) methodValue = body.value();
        if (apiRequest != null && StringTools.isEmpty(methodValue)) methodValue = apiRequest.value();

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

    protected RequestMappingInfo createRequestMappingInfoByPrinter(Body requestMapping, RequestCondition<?> customCondition, String[] path) {
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

    @Override
    protected CorsConfiguration initCorsConfiguration(Object handler, Method method, RequestMappingInfo mappingInfo) {
        HandlerMethod handlerMethod = createHandlerMethod(handler, method);
        CrossOrigin typeAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), CrossOrigin.class);
        CrossOrigin methodAnnotation = AnnotationUtils.findAnnotation(method, CrossOrigin.class);

        if (typeAnnotation == null && methodAnnotation == null) {
            return null;
        }

        CorsConfiguration config = new CorsConfiguration();
        updateCorsConfig(config, typeAnnotation);
        updateCorsConfig(config, methodAnnotation);

        if (CollectionUtils.isEmpty(config.getAllowedOrigins())) {
            config.setAllowedOrigins(Arrays.asList(CrossOrigin.DEFAULT_ORIGINS));
        }
        if (CollectionUtils.isEmpty(config.getAllowedMethods())) {
            for (RequestMethod allowedMethod : mappingInfo.getMethodsCondition().getMethods()) {
                config.addAllowedMethod(allowedMethod.name());
            }
        }
        if (CollectionUtils.isEmpty(config.getAllowedHeaders())) {
            config.setAllowedHeaders(Arrays.asList(CrossOrigin.DEFAULT_ALLOWED_HEADERS));
        }
        if (config.getAllowCredentials() == null) {
            config.setAllowCredentials(CrossOrigin.DEFAULT_ALLOW_CREDENTIALS);
        }
        if (config.getMaxAge() == null) {
            config.setMaxAge(CrossOrigin.DEFAULT_MAX_AGE);
        }
        return config;
    }

    private void updateCorsConfig(CorsConfiguration config, CrossOrigin annotation) {
        if (annotation == null) {
            return;
        }
        for (String origin : annotation.origins()) {
            config.addAllowedOrigin(origin);
        }
        for (RequestMethod method : annotation.methods()) {
            config.addAllowedMethod(method.name());
        }
        for (String header : annotation.allowedHeaders()) {
            config.addAllowedHeader(header);
        }
        for (String header : annotation.exposedHeaders()) {
            config.addExposedHeader(header);
        }

        String allowCredentials = annotation.allowCredentials();
        if ("true".equalsIgnoreCase(allowCredentials)) {
            config.setAllowCredentials(true);
        } else if ("false".equalsIgnoreCase(allowCredentials)) {
            config.setAllowCredentials(false);
        } else if (!allowCredentials.isEmpty()) {
            throw new IllegalStateException("@CrossOrigin's allowCredentials value must be \"true\", \"false\", "
                                            + "or an empty string (\"\"); current value is [" + allowCredentials + "].");
        }

        if (annotation.maxAge() >= 0 && config.getMaxAge() == null) {
            config.setMaxAge(annotation.maxAge());
        }
    }

    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        return super.getHandlerInternal(request);
    }

}
