/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mimosaframework.core.json.parser;

import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessControlException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.ModelPath;
import org.mimosaframework.core.json.annotation.JSONField;
import org.mimosaframework.core.json.annotation.JSONType;
import org.mimosaframework.core.json.parser.deserializer.ASMDeserializerFactory;
import org.mimosaframework.core.json.parser.deserializer.ArrayListTypeFieldDeserializer;
import org.mimosaframework.core.json.parser.deserializer.AutowiredObjectDeserializer;
import org.mimosaframework.core.json.parser.deserializer.DefaultFieldDeserializer;
import org.mimosaframework.core.json.parser.deserializer.EnumDeserializer;
import org.mimosaframework.core.json.parser.deserializer.FieldDeserializer;
import org.mimosaframework.core.json.parser.deserializer.JavaBeanDeserializer;
import org.mimosaframework.core.json.parser.deserializer.JavaObjectDeserializer;
import org.mimosaframework.core.json.parser.deserializer.Jdk8DateCodec;
import org.mimosaframework.core.json.parser.deserializer.MapDeserializer;
import org.mimosaframework.core.json.parser.deserializer.NumberDeserializer;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.parser.deserializer.OptionalCodec;
import org.mimosaframework.core.json.parser.deserializer.SqlDateDeserializer;
import org.mimosaframework.core.json.parser.deserializer.StackTraceElementDeserializer;
import org.mimosaframework.core.json.parser.deserializer.ThrowableDeserializer;
import org.mimosaframework.core.json.parser.deserializer.TimeDeserializer;
import org.mimosaframework.core.json.serializer.AtomicCodec;
import org.mimosaframework.core.json.serializer.AwtCodec;
import org.mimosaframework.core.json.serializer.BigDecimalCodec;
import org.mimosaframework.core.json.serializer.BigIntegerCodec;
import org.mimosaframework.core.json.serializer.BooleanCodec;
import org.mimosaframework.core.json.serializer.CalendarCodec;
import org.mimosaframework.core.json.serializer.CharArrayCodec;
import org.mimosaframework.core.json.serializer.CharacterCodec;
import org.mimosaframework.core.json.serializer.CollectionCodec;
import org.mimosaframework.core.json.serializer.DateCodec;
import org.mimosaframework.core.json.serializer.FloatCodec;
import org.mimosaframework.core.json.serializer.IntegerCodec;
import org.mimosaframework.core.json.serializer.LongCodec;
import org.mimosaframework.core.json.serializer.MiscCodec;
import org.mimosaframework.core.json.serializer.ObjectArrayCodec;
import org.mimosaframework.core.json.serializer.ReferenceCodec;
import org.mimosaframework.core.json.serializer.StringCodec;
import org.mimosaframework.core.json.util.ASMClassLoader;
import org.mimosaframework.core.json.util.ASMUtils;
import org.mimosaframework.core.json.util.FieldInfo;
import org.mimosaframework.core.json.util.IdentityHashMap;
import org.mimosaframework.core.json.util.JavaBeanInfo;
import org.mimosaframework.core.json.util.ServiceLoader;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ParserConfig {

    public final static String DENY_PROPERTY = "fastjson.parser.deny";

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public static ParserConfig                              global      = new ParserConfig();

    private final IdentityHashMap<Type, ObjectDeserializer> derializers = new IdentityHashMap<Type, ObjectDeserializer>();

    private boolean                                         asmEnable   = !ASMUtils.IS_ANDROID;

    public final SymbolTable                                symbolTable = new SymbolTable(4096);

    protected ClassLoader                                   defaultClassLoader;

    protected ASMDeserializerFactory asmFactory;

    private static boolean                                  awtError    = false;
    private static boolean                                  jdk8Error   = false;

    private String[]                                        denyList    = new String[] { "java.lang.Thread" };

    public ParserConfig(){
        this(null, null);
    }

    public ParserConfig(ClassLoader parentClassLoader){
        this(null, parentClassLoader);
    }

    public ParserConfig(ASMDeserializerFactory asmFactory){
        this(asmFactory, null);
    }

    private ParserConfig(ASMDeserializerFactory asmFactory, ClassLoader parentClassLoader){
        if (asmFactory == null && !ASMUtils.IS_ANDROID) {
            try {
                if (parentClassLoader == null) {
                    asmFactory = new ASMDeserializerFactory(new ASMClassLoader());
                } else {
                    asmFactory = new ASMDeserializerFactory(parentClassLoader);
                }
            } catch (ExceptionInInitializerError error) {
                // skip
            } catch (AccessControlException error) {
                // skip
            } catch (NoClassDefFoundError error) {
                // skip
            }
        }

        this.asmFactory = asmFactory;

        if (asmFactory == null) {
            asmEnable = false;
        }

        derializers.put(SimpleDateFormat.class, MiscCodec.instance);
        derializers.put(java.sql.Timestamp.class, SqlDateDeserializer.instance_timestamp);
        derializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
        derializers.put(java.sql.Time.class, TimeDeserializer.instance);
        derializers.put(java.util.Date.class, DateCodec.instance);
        derializers.put(Calendar.class, CalendarCodec.instance);

        derializers.put(ModelObject.class, MapDeserializer.instance);
        derializers.put(ModelArray.class, CollectionCodec.instance);

        derializers.put(Map.class, MapDeserializer.instance);
        derializers.put(HashMap.class, MapDeserializer.instance);
        derializers.put(LinkedHashMap.class, MapDeserializer.instance);
        derializers.put(TreeMap.class, MapDeserializer.instance);
        derializers.put(ConcurrentMap.class, MapDeserializer.instance);
        derializers.put(ConcurrentHashMap.class, MapDeserializer.instance);

        derializers.put(Collection.class, CollectionCodec.instance);
        derializers.put(List.class, CollectionCodec.instance);
        derializers.put(ArrayList.class, CollectionCodec.instance);

        derializers.put(Object.class, JavaObjectDeserializer.instance);
        derializers.put(String.class, StringCodec.instance);
        derializers.put(StringBuffer.class, StringCodec.instance);
        derializers.put(StringBuilder.class, StringCodec.instance);
        derializers.put(char.class, CharacterCodec.instance);
        derializers.put(Character.class, CharacterCodec.instance);
        derializers.put(byte.class, NumberDeserializer.instance);
        derializers.put(Byte.class, NumberDeserializer.instance);
        derializers.put(short.class, NumberDeserializer.instance);
        derializers.put(Short.class, NumberDeserializer.instance);
        derializers.put(int.class, IntegerCodec.instance);
        derializers.put(Integer.class, IntegerCodec.instance);
        derializers.put(long.class, LongCodec.instance);
        derializers.put(Long.class, LongCodec.instance);
        derializers.put(BigInteger.class, BigIntegerCodec.instance);
        derializers.put(BigDecimal.class, BigDecimalCodec.instance);
        derializers.put(float.class, FloatCodec.instance);
        derializers.put(Float.class, FloatCodec.instance);
        derializers.put(double.class, NumberDeserializer.instance);
        derializers.put(Double.class, NumberDeserializer.instance);
        derializers.put(boolean.class, BooleanCodec.instance);
        derializers.put(Boolean.class, BooleanCodec.instance);
        derializers.put(Class.class, MiscCodec.instance);
        derializers.put(char[].class, new CharArrayCodec());

        derializers.put(AtomicBoolean.class, BooleanCodec.instance);
        derializers.put(AtomicInteger.class, IntegerCodec.instance);
        derializers.put(AtomicLong.class, LongCodec.instance);
        derializers.put(AtomicReference.class, ReferenceCodec.instance);

        derializers.put(WeakReference.class, ReferenceCodec.instance);
        derializers.put(SoftReference.class, ReferenceCodec.instance);

        derializers.put(UUID.class, MiscCodec.instance);
        derializers.put(TimeZone.class, MiscCodec.instance);
        derializers.put(Locale.class, MiscCodec.instance);
        derializers.put(Currency.class, MiscCodec.instance);
        derializers.put(InetAddress.class, MiscCodec.instance);
        derializers.put(Inet4Address.class, MiscCodec.instance);
        derializers.put(Inet6Address.class, MiscCodec.instance);
        derializers.put(InetSocketAddress.class, MiscCodec.instance);
        derializers.put(File.class, MiscCodec.instance);
        derializers.put(URI.class, MiscCodec.instance);
        derializers.put(URL.class, MiscCodec.instance);
        derializers.put(Pattern.class, MiscCodec.instance);
        derializers.put(Charset.class, MiscCodec.instance);
        derializers.put(ModelPath.class, MiscCodec.instance);
        derializers.put(Number.class, NumberDeserializer.instance);
        derializers.put(AtomicIntegerArray.class, AtomicCodec.instance);
        derializers.put(AtomicLongArray.class, AtomicCodec.instance);
        derializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);

        derializers.put(Serializable.class, JavaObjectDeserializer.instance);
        derializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        derializers.put(Comparable.class, JavaObjectDeserializer.instance);
        derializers.put(Closeable.class, JavaObjectDeserializer.instance);

        addDeny("java.lang.Thread");
        configFromPropety(System.getProperties());
    }

    public void configFromPropety(Properties properties) {
        String property = properties.getProperty(DENY_PROPERTY);
        if (property != null && property.length() > 0) {
            String[] items = property.split(",");
            for (int i = 0; i < items.length; ++i) {
                String item = items[i];
                this.addDeny(item);
            }
        }
    }

    public boolean isAsmEnable() {
        return asmEnable;
    }

    public void setAsmEnable(boolean asmEnable) {
        this.asmEnable = asmEnable;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return derializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer derializer = this.derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type instanceof Class<?>) {
            return getDeserializer((Class<?>) type, type);
        }

        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class<?>) {
                return getDeserializer((Class<?>) rawType, type);
            } else {
                return getDeserializer(rawType);
            }
        }

        return JavaObjectDeserializer.instance;
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        ObjectDeserializer derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type == null) {
            type = clazz;
        }

        derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        {
            JSONType annotation = clazz.getAnnotation(JSONType.class);
            if (annotation != null) {
                Class<?> mappingTo = annotation.mappingTo();
                if (mappingTo != Void.class) {
                    return getDeserializer(mappingTo, mappingTo);
                }
            }
        }

        if (type instanceof WildcardType || type instanceof TypeVariable || type instanceof ParameterizedType) {
            derializer = derializers.get(clazz);
        }

        if (derializer != null) {
            return derializer;
        }

        String className = clazz.getName();
        for (int i = 0; i < denyList.length; ++i) {
            String deny = denyList[i];
            className = className.replace('$', '.');
            if (className.startsWith(deny)) {
                throw new ModelException("parser deny : " + className);
            }
        }

        if (className.startsWith("java.awt.") //
            && AwtCodec.support(clazz)) {
            if (!awtError) {
                try {
                    derializers.put(Class.forName("java.awt.Point"), AwtCodec.instance);
                    derializers.put(Class.forName("java.awt.Font"), AwtCodec.instance);
                    derializers.put(Class.forName("java.awt.Rectangle"), AwtCodec.instance);
                    derializers.put(Class.forName("java.awt.Color"), AwtCodec.instance);
                } catch (Throwable e) {
                    // skip
                    awtError = true;
                }

                derializer = AwtCodec.instance;
            }
        }

        if (!jdk8Error) {
            try {
                if (className.startsWith("java.time.")) {
                    
                    derializers.put(Class.forName("java.time.LocalDateTime"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.LocalDate"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.LocalTime"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.ZonedDateTime"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.OffsetDateTime"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.OffsetTime"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.ZoneOffset"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.ZoneRegion"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.ZoneId"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.Period"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.Duration"), Jdk8DateCodec.instance);
                    derializers.put(Class.forName("java.time.Instant"), Jdk8DateCodec.instance);
                    
                    derializer = derializers.get(clazz);
                } else if (className.startsWith("java.util.Optional")) {
                    
                    derializers.put(Class.forName("java.util.Optional"), OptionalCodec.instance);
                    derializers.put(Class.forName("java.util.OptionalDouble"), OptionalCodec.instance);
                    derializers.put(Class.forName("java.util.OptionalInt"), OptionalCodec.instance);
                    derializers.put(Class.forName("java.util.OptionalLong"), OptionalCodec.instance);
                    
                    derializer = derializers.get(clazz);
                }
            } catch (Throwable e) {
                // skip
                jdk8Error = true;
            }
        }

        if (className.equals("java.nio.file.Path")) {
            derializers.put(clazz, MiscCodec.instance);
        }

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            for (AutowiredObjectDeserializer autowired : ServiceLoader.load(AutowiredObjectDeserializer.class,
                                                                            classLoader)) {
                for (Type forType : autowired.getAutowiredFor()) {
                    derializers.put(forType, autowired);
                }
            }
        } catch (Exception ex) {
            // skip
        }

        if (derializer == null) {
            derializer = derializers.get(type);
        }

        if (derializer != null) {
            return derializer;
        }

        if (clazz.isEnum()) {
            derializer = new EnumDeserializer(clazz);
        } else if (clazz.isArray()) {
            derializer = ObjectArrayCodec.instance;
        } else if (clazz == Set.class || clazz == HashSet.class || clazz == Collection.class || clazz == List.class
                   || clazz == ArrayList.class) {
            derializer = CollectionCodec.instance;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            derializer = CollectionCodec.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            derializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            derializer = new ThrowableDeserializer(this, clazz);
        } else {
            derializer = createJavaBeanDeserializer(clazz, type);
        }

        putDeserializer(type, derializer);

        return derializer;
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        boolean asmEnable = this.asmEnable;
        if (asmEnable) {
            JSONType jsonType = clazz.getAnnotation(JSONType.class);

            if (jsonType != null && !jsonType.asm()) {
                asmEnable = false;
            }

            if (asmEnable) {
                Class<?> superClass = JavaBeanInfo.getBuilderClass(jsonType);
                if (superClass == null) {
                    superClass = clazz;
                }

                for (;;) {
                    if (!Modifier.isPublic(superClass.getModifiers())) {
                        asmEnable = false;
                        break;
                    }

                    superClass = superClass.getSuperclass();
                    if (superClass == Object.class || superClass == null) {
                        break;
                    }
                }
            }
        }

        if (clazz.getTypeParameters().length != 0) {
            asmEnable = false;
        }

        if (asmEnable && asmFactory != null && asmFactory.classLoader.isExternalClass(clazz)) {
            asmEnable = false;
        }

        if (asmEnable) {
            asmEnable = ASMUtils.checkName(clazz.getName());
        }

        if (asmEnable) {
            if (clazz.isInterface()) {
                asmEnable = false;
            }
            JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type);

            if (asmEnable && beanInfo.fields.length > 200) {
                asmEnable = false;
            }

            Constructor<?> defaultConstructor = beanInfo.defaultConstructor;
            if (asmEnable && defaultConstructor == null && !clazz.isInterface()) {
                asmEnable = false;
            }

            for (FieldInfo fieldInfo : beanInfo.fields) {
                if (fieldInfo.getOnly) {
                    asmEnable = false;
                    break;
                }

                Class<?> fieldClass = fieldInfo.fieldClass;
                if (!Modifier.isPublic(fieldClass.getModifiers())) {
                    asmEnable = false;
                    break;
                }

                if (fieldClass.isMemberClass() && !Modifier.isStatic(fieldClass.getModifiers())) {
                    asmEnable = false;
                    break;
                }

                if (fieldInfo.getMember() != null //
                    && !ASMUtils.checkName(fieldInfo.getMember().getName())) {
                    asmEnable = false;
                    break;
                }

                JSONField annotation = fieldInfo.getAnnotation();
                if (annotation != null //
                    && ((!ASMUtils.checkName(annotation.name())) //
                        || annotation.format().length() != 0)) {
                    asmEnable = false;
                    break;
                }

                if (fieldClass.isEnum()) { // EnumDeserializer
                    ObjectDeserializer fieldDeser = this.getDeserializer(fieldClass);
                    if (!(fieldDeser instanceof EnumDeserializer)) {
                        asmEnable = false;
                        break;
                    }
                }
            }
        }

        if (asmEnable) {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                asmEnable = false;
            }
        }

        if (!asmEnable) {
            return new JavaBeanDeserializer(this, clazz, type);
        }

        JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type);
        try {
            return asmFactory.createJavaBeanDeserializer(this, beanInfo);
            // } catch (VerifyError e) {
            // e.printStackTrace();
            // return new JavaBeanDeserializer(this, clazz, type);
        } catch (NoSuchMethodException ex) {
            return new JavaBeanDeserializer(this, clazz, type);
        } catch (ModelException asmError) {
            return new JavaBeanDeserializer(this, beanInfo);
        } catch (Exception e) {
            throw new ModelException("create asm deserializer error, " + clazz.getName(), e);
        }
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, //
                                                     JavaBeanInfo beanInfo, //
                                                     FieldInfo fieldInfo) {
        Class<?> clazz = beanInfo.clazz;
        Class<?> fieldClass = fieldInfo.fieldClass;

        if (fieldClass == List.class || fieldClass == ArrayList.class) {
            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }

        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
        derializers.put(type, deserializer);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.fieldClass, fieldInfo.fieldType);
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() //
               || clazz == Boolean.class //
               || clazz == Character.class //
               || clazz == Byte.class //
               || clazz == Short.class //
               || clazz == Integer.class //
               || clazz == Long.class //
               || clazz == Float.class //
               || clazz == Double.class //
               || clazz == BigInteger.class //
               || clazz == BigDecimal.class //
               || clazz == String.class //
               || clazz == java.util.Date.class //
               || clazz == java.sql.Date.class //
               || clazz == java.sql.Time.class //
               || clazz == java.sql.Timestamp.class //
               || clazz.isEnum() //
        ;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = getField0(clazz, fieldName);

        if (field == null) {
            field = getField0(clazz, "_" + fieldName);
        }

        if (field == null) {
            field = getField0(clazz, "m_" + fieldName);
        }

        return field;
    }

    private static Field getField0(Class<?> clazz, String fieldName) {
        for (Field item : clazz.getDeclaredFields()) {
            if (fieldName.equals(item.getName())) {
                return item;
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            return getField(clazz.getSuperclass(), fieldName);
        }

        return null;
    }

    public ClassLoader getDefaultClassLoader() {
        return defaultClassLoader;
    }

    public void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        this.defaultClassLoader = defaultClassLoader;
    }

    public void addDeny(String name) {
        if (name == null || name.length() == 0) {
            return;
        }

        String[] denyList = new String[this.denyList.length + 1];
        System.arraycopy(this.denyList, 0, denyList, 0, this.denyList.length);
        denyList[denyList.length - 1] = name;
        this.denyList = denyList;
    }
}
