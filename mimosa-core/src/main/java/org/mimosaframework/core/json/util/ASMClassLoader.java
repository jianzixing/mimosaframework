package org.mimosaframework.core.json.util;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.json.ModelAware;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.ModelPath;
import org.mimosaframework.core.json.ModelPathException;
import org.mimosaframework.core.json.ModelReader;
import org.mimosaframework.core.json.ModelStreamAware;
import org.mimosaframework.core.json.ModelWriter;
import org.mimosaframework.core.json.TypeReference;
import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.ModelLexerBase;
import org.mimosaframework.core.json.parser.ModelReaderScanner;
import org.mimosaframework.core.json.parser.ModelScanner;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.parser.ParseContext;
import org.mimosaframework.core.json.parser.ParserConfig;
import org.mimosaframework.core.json.parser.SymbolTable;
import org.mimosaframework.core.json.parser.deserializer.AutowiredObjectDeserializer;
import org.mimosaframework.core.json.parser.deserializer.DefaultFieldDeserializer;
import org.mimosaframework.core.json.parser.deserializer.ExtraProcessable;
import org.mimosaframework.core.json.parser.deserializer.ExtraProcessor;
import org.mimosaframework.core.json.parser.deserializer.ExtraTypeProvider;
import org.mimosaframework.core.json.parser.deserializer.FieldDeserializer;
import org.mimosaframework.core.json.parser.deserializer.JavaBeanDeserializer;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.serializer.AfterFilter;
import org.mimosaframework.core.json.serializer.BeanContext;
import org.mimosaframework.core.json.serializer.BeforeFilter;
import org.mimosaframework.core.json.serializer.ContextObjectSerializer;
import org.mimosaframework.core.json.serializer.ContextValueFilter;
import org.mimosaframework.core.json.serializer.ModelSerializer;
import org.mimosaframework.core.json.serializer.JavaBeanSerializer;
import org.mimosaframework.core.json.serializer.LabelFilter;
import org.mimosaframework.core.json.serializer.Labels;
import org.mimosaframework.core.json.serializer.NameFilter;
import org.mimosaframework.core.json.serializer.ObjectSerializer;
import org.mimosaframework.core.json.serializer.PropertyFilter;
import org.mimosaframework.core.json.serializer.PropertyPreFilter;
import org.mimosaframework.core.json.serializer.SerialContext;
import org.mimosaframework.core.json.serializer.SerializeBeanInfo;
import org.mimosaframework.core.json.serializer.SerializeConfig;
import org.mimosaframework.core.json.serializer.SerializeFilter;
import org.mimosaframework.core.json.serializer.SerializeFilterable;
import org.mimosaframework.core.json.serializer.SerializeWriter;
import org.mimosaframework.core.json.serializer.SerializerFeature;
import org.mimosaframework.core.json.serializer.ValueFilter;

public class ASMClassLoader extends ClassLoader {

    private static java.security.ProtectionDomain DOMAIN;
    
    private static Map<String, Class<?>> classMapping = new HashMap<String, Class<?>>();

    static {
        DOMAIN = (java.security.ProtectionDomain) java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                return ASMClassLoader.class.getProtectionDomain();
            }
        });
        
        Class<?>[] jsonClasses = new Class<?>[] {Model.class,
            ModelObject.class,
            ModelArray.class,
            ModelPath.class,
            ModelAware.class,
            ModelException.class,
            ModelPathException.class,
            ModelReader.class,
            ModelStreamAware.class,
            ModelWriter.class,
            TypeReference.class,
                    
            FieldInfo.class,
            TypeUtils.class,
            IOUtils.class,
            IdentityHashMap.class,
            ParameterizedTypeImpl.class,
            JavaBeanInfo.class,
                    
            ObjectSerializer.class,
            JavaBeanSerializer.class,
            SerializeFilterable.class,
            SerializeBeanInfo.class,
            ModelSerializer.class,
            SerializeWriter.class,
            SerializeFilter.class,
            Labels.class,
            LabelFilter.class,
            ContextValueFilter.class,
            AfterFilter.class,
            BeforeFilter.class,
            NameFilter.class,
            PropertyFilter.class,
            PropertyPreFilter.class,
            ValueFilter.class,
            SerializerFeature.class,
            ContextObjectSerializer.class,
            SerialContext.class,
            SerializeConfig.class,
                    
            JavaBeanDeserializer.class,
            ParserConfig.class,
            DefaultModelParser.class,
            ModelLexer.class,
            ModelLexerBase.class,
            ParseContext.class,
            ModelToken.class,
            SymbolTable.class,
            Feature.class,
            ModelScanner.class,
            ModelReaderScanner.class,
                    
            AutowiredObjectDeserializer.class,
            ObjectDeserializer.class,
            ExtraProcessor.class,
            ExtraProcessable.class,
            ExtraTypeProvider.class,
            BeanContext.class,
            FieldDeserializer.class,
            DefaultFieldDeserializer.class,
        };
        
        for (Class<?> clazz : jsonClasses) {
            classMapping.put(clazz.getName(), clazz);
        }
    }
    
    public ASMClassLoader(){
        super(getParentClassLoader());
    }

    public ASMClassLoader(ClassLoader parent){
        super (parent);
    }

    static ClassLoader getParentClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                contextClassLoader.loadClass(Model.class.getName());
                return contextClassLoader;
            } catch (ClassNotFoundException e) {
                // skip
            }
        }
        return Model.class.getClassLoader();
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> mappingClass = classMapping.get(name);
        if (mappingClass != null) {
            return mappingClass;
        }
        
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        Class<?> clazz = defineClass(name, b, off, len, DOMAIN);

        return clazz;
    }

    public boolean isExternalClass(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();

        if (classLoader == null) {
            return false;
        }

        ClassLoader current = this;
        while (current != null) {
            if (current == classLoader) {
                return false;
            }

            current = current.getParent();
        }

        return true;
    }

}
