package org.mimosaframework.core.json.serializer;

/**
 * @since 1.2.9
 *
 */
public interface ContextValueFilter extends SerializeFilter {
    Object process(BeanContext context, Object object, String name, Object value);
}
