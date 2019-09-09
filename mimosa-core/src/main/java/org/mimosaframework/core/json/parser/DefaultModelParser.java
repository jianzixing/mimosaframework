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

import static org.mimosaframework.core.json.parser.ModelToken.EOF;
import static org.mimosaframework.core.json.parser.ModelToken.ERROR;
import static org.mimosaframework.core.json.parser.ModelToken.FALSE;
import static org.mimosaframework.core.json.parser.ModelToken.LBRACE;
import static org.mimosaframework.core.json.parser.ModelToken.LBRACKET;
import static org.mimosaframework.core.json.parser.ModelToken.LITERAL_FLOAT;
import static org.mimosaframework.core.json.parser.ModelToken.LITERAL_INT;
import static org.mimosaframework.core.json.parser.ModelToken.LITERAL_STRING;
import static org.mimosaframework.core.json.parser.ModelToken.NEW;
import static org.mimosaframework.core.json.parser.ModelToken.NULL;
import static org.mimosaframework.core.json.parser.ModelToken.RBRACKET;
import static org.mimosaframework.core.json.parser.ModelToken.SET;
import static org.mimosaframework.core.json.parser.ModelToken.TREE_SET;
import static org.mimosaframework.core.json.parser.ModelToken.TRUE;
import static org.mimosaframework.core.json.parser.ModelToken.UNDEFINED;

import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.deserializer.ExtraProcessable;
import org.mimosaframework.core.json.parser.deserializer.ExtraProcessor;
import org.mimosaframework.core.json.parser.deserializer.ExtraTypeProvider;
import org.mimosaframework.core.json.parser.deserializer.FieldDeserializer;
import org.mimosaframework.core.json.parser.deserializer.FieldTypeResolver;
import org.mimosaframework.core.json.parser.deserializer.JavaBeanDeserializer;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.parser.deserializer.ResolveFieldDeserializer;
import org.mimosaframework.core.json.serializer.BeanContext;
import org.mimosaframework.core.json.serializer.IntegerCodec;
import org.mimosaframework.core.json.serializer.LongCodec;
import org.mimosaframework.core.json.serializer.StringCodec;
import org.mimosaframework.core.json.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class DefaultModelParser implements Closeable {

    public final Object                input;
    public final SymbolTable           symbolTable;
    protected ParserConfig             config;

    private final static Set<Class<?>> primitiveClasses   = new HashSet<Class<?>>();

    private String                     dateFormatPattern  = Model.DEFFAULT_DATE_FORMAT;
    private DateFormat                 dateFormat;

    public final ModelLexer lexer;

    protected ParseContext             context;

    private ParseContext[]             contextArray;
    private int                        contextArrayIndex  = 0;

    private List<ResolveTask>          resolveTaskList;

    public final static int            NONE               = 0;
    public final static int            NeedToResolve      = 1;
    public final static int            TypeNameRedirect   = 2;

    public int                         resolveStatus      = NONE;

    private List<ExtraTypeProvider>    extraTypeProviders = null;
    private List<ExtraProcessor>       extraProcessors    = null;
    protected FieldTypeResolver        fieldTypeResolver  = null;
    
    protected transient BeanContext    lastBeanContext;

    static {
        primitiveClasses.add(boolean.class);
        primitiveClasses.add(byte.class);
        primitiveClasses.add(short.class);
        primitiveClasses.add(int.class);
        primitiveClasses.add(long.class);
        primitiveClasses.add(float.class);
        primitiveClasses.add(double.class);

        primitiveClasses.add(Boolean.class);
        primitiveClasses.add(Byte.class);
        primitiveClasses.add(Short.class);
        primitiveClasses.add(Integer.class);
        primitiveClasses.add(Long.class);
        primitiveClasses.add(Float.class);
        primitiveClasses.add(Double.class);

        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);
        primitiveClasses.add(String.class);
    }

    public String getDateFomartPattern() {
        return dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(dateFormatPattern, lexer.getLocale());
            dateFormat.setTimeZone(lexer.getTimeZone());
        }
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormatPattern = dateFormat;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultModelParser(String input){
        this(input, ParserConfig.getGlobalInstance(), Model.DEFAULT_PARSER_FEATURE);
    }

    public DefaultModelParser(final String input, final ParserConfig config){
        this(input, new ModelScanner(input, Model.DEFAULT_PARSER_FEATURE), config);
    }

    public DefaultModelParser(final String input, final ParserConfig config, int features){
        this(input, new ModelScanner(input, features), config);
    }

    public DefaultModelParser(final char[] input, int length, final ParserConfig config, int features){
        this(input, new ModelScanner(input, length, features), config);
    }

    public DefaultModelParser(final ModelLexer lexer){
        this(lexer, ParserConfig.getGlobalInstance());
    }

    public DefaultModelParser(final ModelLexer lexer, final ParserConfig config){
        this(null, lexer, config);
    }

    public DefaultModelParser(final Object input, final ModelLexer lexer, final ParserConfig config){
        this.lexer = lexer;
        this.input = input;
        this.config = config;
        this.symbolTable = config.symbolTable;

        int ch = lexer.getCurrent();
        if (ch == '{') {
            lexer.next();
            ((ModelLexerBase) lexer).token = ModelToken.LBRACE;
        } else if (ch == '[') {
            lexer.next();
            ((ModelLexerBase) lexer).token = ModelToken.LBRACKET;
        } else {
            lexer.nextToken(); // prime the pump
        }
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public String getInput() {
        if (input instanceof char[]) {
            return new String((char[]) input);
        }
        return input.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Object parseObject(final Map object, Object fieldName) {
        final ModelLexer lexer = this.lexer;
        
        if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken();
            return null;
        }
        
        if (lexer.token() == ModelToken.RBRACE) {
            lexer.nextToken();
            return object;
        }

        if (lexer.token() != ModelToken.LBRACE && lexer.token() != ModelToken.COMMA) {
            throw new ModelException("syntax error, expect {, actual " + lexer.tokenName() + ", " + lexer.info());
        }

       ParseContext context = this.context;
        try {
            boolean setContextFlag = false;
            for (;;) {
                lexer.skipWhitespace();
                char ch = lexer.getCurrent();
                if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (ch == ',') {
                        lexer.next();
                        lexer.skipWhitespace();
                        ch = lexer.getCurrent();
                    }
                }

                boolean isObjectKey = false;
                Object key;
                if (ch == '"') {
                    key = lexer.scanSymbol(symbolTable, '"');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new ModelException("expect ':' at " + lexer.pos() + ", name " + key);
                    }
                } else if (ch == '}') {
                    lexer.next();
                    lexer.resetStringPosition();
                    lexer.nextToken();
                    return object;
                } else if (ch == '\'') {
                    if (!lexer.isEnabled(Feature.AllowSingleQuotes)) {
                        throw new ModelException("syntax error");
                    }

                    key = lexer.scanSymbol(symbolTable, '\'');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new ModelException("expect ':' at " + lexer.pos());
                    }
                } else if (ch == ModelLexer.EOI) {
                    throw new ModelException("syntax error");
                } else if (ch == ',') {
                    throw new ModelException("syntax error");
                } else if ((ch >= '0' && ch <= '9') || ch == '-') {
                    lexer.resetStringPosition();
                    lexer.scanNumber();
                    try {
                    if (lexer.token() == ModelToken.LITERAL_INT) {
                        key = lexer.integerValue();
                    } else {
                        key = lexer.decimalValue(true);
                    }
                    } catch (NumberFormatException e) {
                        throw new ModelException("parse number key error" + lexer.info());
                    }
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new ModelException("parse number key error" + lexer.info());
                    }
                } else if (ch == '{' || ch == '[') {
                    lexer.nextToken();
                    key = parse();
                    isObjectKey = true;
                } else {
                    if (!lexer.isEnabled(Feature.AllowUnQuotedFieldNames)) {
                        throw new ModelException("syntax error");
                    }

                    key = lexer.scanSymbolUnQuoted(symbolTable);
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new ModelException("expect ':' at " + lexer.pos() + ", actual " + ch);
                    }
                }

                if (!isObjectKey) {
                    lexer.next();
                    lexer.skipWhitespace();
                }

                ch = lexer.getCurrent();

                lexer.resetStringPosition();

                if (key == Model.DEFAULT_TYPE_KEY && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    String typeName = lexer.scanSymbol(symbolTable, '"');
                    Class<?> clazz = TypeUtils.loadClass(typeName, config.getDefaultClassLoader());

                    if (clazz == null) {
                        object.put(Model.DEFAULT_TYPE_KEY, typeName);
                        continue;
                    }

                    lexer.nextToken(ModelToken.COMMA);
                    if (lexer.token() == ModelToken.RBRACE) {
                        lexer.nextToken(ModelToken.COMMA);
                        try {
                            Object instance = null;
                            ObjectDeserializer deserializer = this.config.getDeserializer(clazz);
                            if (deserializer instanceof JavaBeanDeserializer) {
                                instance = ((JavaBeanDeserializer) deserializer).createInstance(this, clazz);
                            }

                            if (instance == null) {
                                if (clazz == Cloneable.class) {
                                    instance = new HashMap();
                                } else if ("java.util.Collections$EmptyMap".equals(typeName)) {
                                    instance = Collections.emptyMap();
                                } else {
                                    instance = clazz.newInstance();
                                }
                            }

                            return instance;
                        } catch (Exception e) {
                            throw new ModelException("create instance error", e);
                        }
                    }
                    
                    this.setResolveStatus(TypeNameRedirect);

                    if (this.context != null && !(fieldName instanceof Integer)) {
                        this.popContext();
                    }
                    
                    if (object.size() > 0) {
                        Object newObj = TypeUtils.cast(object, clazz, this.config);
                        this.parseObject(newObj);
                        return newObj;
                    }

                    ObjectDeserializer deserializer = config.getDeserializer(clazz);
                    return deserializer.deserialze(this, clazz, fieldName);
                }

                if (key == "$ref" && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    lexer.nextToken(ModelToken.LITERAL_STRING);
                    if (lexer.token() == ModelToken.LITERAL_STRING) {
                        String ref = lexer.stringVal();
                        lexer.nextToken(ModelToken.RBRACE);

                        Object refValue = null;
                        if ("@".equals(ref)) {
                            if (this.context != null) {
                                ParseContext thisContext = this.context;
                                Object thisObj = thisContext.object;
                                if (thisObj instanceof Object[] || thisObj instanceof Collection<?>) {
                                    refValue = thisObj;
                                } else if (thisContext.parent != null) {
                                    refValue = thisContext.parent.object;
                                }
                            }
                        } else if ("..".equals(ref)) {
                            if (context.object != null) {
                                refValue = context.object;
                            } else {
                                addResolveTask(new ResolveTask(context, ref));
                                setResolveStatus(DefaultModelParser.NeedToResolve);
                            }
                        } else if ("$".equals(ref)) {
                            ParseContext rootContext = context;
                            while (rootContext.parent != null) {
                                rootContext = rootContext.parent;
                            }

                            if (rootContext.object != null) {
                                refValue = rootContext.object;
                            } else {
                                addResolveTask(new ResolveTask(rootContext, ref));
                                setResolveStatus(DefaultModelParser.NeedToResolve);
                            }
                        } else {
                            addResolveTask(new ResolveTask(context, ref));
                            setResolveStatus(DefaultModelParser.NeedToResolve);
                        }

                        if (lexer.token() != ModelToken.RBRACE) {
                            throw new ModelException("syntax error");
                        }
                        lexer.nextToken(ModelToken.COMMA);

                        return refValue;
                    } else {
                        throw new ModelException("illegal ref, " + ModelToken.name(lexer.token()));
                    }
                }

                if (!setContextFlag) {
                    ParseContext contextR = setContext(object, fieldName);
                    if (context == null) {
                        context = contextR;
                    }
                    setContextFlag = true;
                }

                if (object.getClass() == ModelObject.class) {
                    key = (key == null) ? "null" : key.toString();
                }

                Object value;
                if (ch == '"') {
                    lexer.scanString();
                    String strValue = lexer.stringVal();
                    value = strValue;

                    if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                        ModelScanner iso8601Lexer = new ModelScanner(strValue);
                        if (iso8601Lexer.scanISO8601DateIfMatch()) {
                            value = iso8601Lexer.getCalendar().getTime();
                        }
                        iso8601Lexer.close();
                    }

                    object.put(key, value);
                } else if (ch >= '0' && ch <= '9' || ch == '-') {
                    lexer.scanNumber();
                    if (lexer.token() == ModelToken.LITERAL_INT) {
                        value = lexer.integerValue();
                    } else {
                        value = lexer.decimalValue(lexer.isEnabled(Feature.UseBigDecimal));
                    }

                    object.put(key, value);
                } else if (ch == '[') { // 减少嵌套，兼容android
                    lexer.nextToken();
                    ModelArray list = new ModelArray();
                    this.parseArray(list, key);
                    
                    if (lexer.isEnabled(Feature.UseObjectArray)) {
                        value = list.toArray();
                    } else {
                        value = list;
                    }
                    object.put(key, value);

                    if (lexer.token() == ModelToken.RBRACE) {
                        lexer.nextToken();
                        return object;
                    } else if (lexer.token() == ModelToken.COMMA) {
                        continue;
                    } else {
                        throw new ModelException("syntax error");
                    }
                } else if (ch == '{') { // 减少嵌套，兼容android
                    lexer.nextToken();

                    final boolean parentIsArray = fieldName != null && fieldName.getClass() == Integer.class;

                    ModelObject input = new ModelObject(lexer.isEnabled(Feature.OrderedField));
                    ParseContext ctxLocal = null;

                    if (!parentIsArray) {
                        ctxLocal = setContext(context, input, key);
                    }

                    Object obj = null;
                    boolean objParsed = false;
                    if (fieldTypeResolver != null) {
                        String resolveFieldName = key != null ? key.toString() : null;
                        Type fieldType = fieldTypeResolver.resolve(object, resolveFieldName);
                        if (fieldType != null) {
                            ObjectDeserializer fieldDeser = config.getDeserializer(fieldType);
                            obj = fieldDeser.deserialze(this, fieldType, key);
                            objParsed = true;
                        }
                    }
                    if (!objParsed) {
                        obj = this.parseObject(input, key);
                    }
                    
                    if (ctxLocal != null && input != obj) {
                        ctxLocal.object = object;
                    }

                    checkMapResolve(object, key.toString());

                    if (object.getClass() == ModelObject.class) {
                        object.put(key.toString(), obj);
                    } else {
                        object.put(key, obj);
                    }

                    if (parentIsArray) {
                        //setContext(context, obj, key);
                        setContext(obj, key);
                    }

                    if (lexer.token() == ModelToken.RBRACE) {
                        lexer.nextToken();

                        setContext(context);
                        return object;
                    } else if (lexer.token() == ModelToken.COMMA) {
                        if (parentIsArray) {
                            this.popContext();
                        }
                        continue;
                    } else {
                        throw new ModelException("syntax error, " + lexer.tokenName());
                    }
                } else {
                    lexer.nextToken();
                    value = parse();

                    if (object.getClass() == ModelObject.class) {
                        key = key.toString();
                    }
                    object.put(key, value);

                    if (lexer.token() == ModelToken.RBRACE) {
                        lexer.nextToken();
                        return object;
                    } else if (lexer.token() == ModelToken.COMMA) {
                        continue;
                    } else {
                        throw new ModelException("syntax error, position at " + lexer.pos() + ", name " + key);
                    }
                }

                lexer.skipWhitespace();
                ch = lexer.getCurrent();
                if (ch == ',') {
                    lexer.next();
                    continue;
                } else if (ch == '}') {
                    lexer.next();
                    lexer.resetStringPosition();
                    lexer.nextToken();

                    // this.setContext(object, fieldName);
                    this.setContext(value, key);

                    return object;
                } else {
                    throw new ModelException("syntax error, position at " + lexer.pos() + ", name " + key);
                }

            }
        } finally {
            this.setContext(context);
        }

    }

    public ParserConfig getConfig() {
        return config;
    }

    public void setConfig(ParserConfig config) {
        this.config = config;
    }

    // compatible
    @SuppressWarnings("unchecked")
    public <T> T parseObject(Class<T> clazz) {
        return (T) parseObject(clazz, null);
    }
    
    public <T> T parseObject(Type type) {
        return parseObject(type, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T parseObject(Type type, Object fieldName) {
        int token = lexer.token();
        if (token == ModelToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (token == ModelToken.LITERAL_STRING) {
            if (type == byte[].class) {
                byte[] bytes = lexer.bytesValue();
                lexer.nextToken();
                return (T) bytes;
            }

            if (type == char[].class) {
                String strVal = lexer.stringVal();
                lexer.nextToken();
                return (T) strVal.toCharArray();
            }
        }

        ObjectDeserializer derializer = config.getDeserializer(type);

        try {
            return (T) derializer.deserialze(this, type, fieldName);
        } catch (ModelException e) {
            throw e;
        } catch (Throwable e) {
            throw new ModelException(e.getMessage(), e);
        }
    }

    public <T> List<T> parseArray(Class<T> clazz) {
        List<T> array = new ArrayList<T>();
        parseArray(clazz, array);
        return array;
    }

    public void parseArray(Class<?> clazz, @SuppressWarnings("rawtypes") Collection array) {
        parseArray((Type) clazz, array);
    }

    @SuppressWarnings("rawtypes")
    public void parseArray(Type type, Collection array) {
        parseArray(type, array, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void parseArray(Type type, Collection array, Object fieldName) {
        if (lexer.token() == ModelToken.SET || lexer.token() == ModelToken.TREE_SET) {
            lexer.nextToken();
        }

        if (lexer.token() != ModelToken.LBRACKET) {
            throw new ModelException("exepct '[', but " + ModelToken.name(lexer.token()) + ", " + lexer.info());
        }

        ObjectDeserializer deserializer = null;
        if (int.class == type) {
            deserializer = IntegerCodec.instance;
            lexer.nextToken(ModelToken.LITERAL_INT);
        } else if (String.class == type) {
            deserializer = StringCodec.instance;
            lexer.nextToken(ModelToken.LITERAL_STRING);
        } else {
            deserializer = config.getDeserializer(type);
            lexer.nextToken(deserializer.getFastMatchToken());
        }

        ParseContext context = this.context;
        this.setContext(array, fieldName);
        try {
            for (int i = 0;; ++i) {
                if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (lexer.token() == ModelToken.COMMA) {
                        lexer.nextToken();
                        continue;
                    }
                }

                if (lexer.token() == ModelToken.RBRACKET) {
                    break;
                }

                if (int.class == type) {
                    Object val = IntegerCodec.instance.deserialze(this, null, null);
                    array.add(val);
                } else if (String.class == type) {
                    String value;
                    if (lexer.token() == ModelToken.LITERAL_STRING) {
                        value = lexer.stringVal();
                        lexer.nextToken(ModelToken.COMMA);
                    } else {
                        Object obj = this.parse();
                        if (obj == null) {
                            value = null;
                        } else {
                            value = obj.toString();
                        }
                    }

                    array.add(value);
                } else {
                    Object val;
                    if (lexer.token() == ModelToken.NULL) {
                        lexer.nextToken();
                        val = null;
                    } else {
                        val = deserializer.deserialze(this, type, i);
                    }
                    array.add(val);
                    checkListResolve(array);
                }

                if (lexer.token() == ModelToken.COMMA) {
                    lexer.nextToken(deserializer.getFastMatchToken());
                    continue;
                }
            }
        } finally {
            this.setContext(context);
        }

        lexer.nextToken(ModelToken.COMMA);
    }

    public Object[] parseArray(Type[] types) {
        if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken(ModelToken.COMMA);
            return null;
        }

        if (lexer.token() != ModelToken.LBRACKET) {
            throw new ModelException("syntax error : " + lexer.tokenName());
        }

        Object[] list = new Object[types.length];
        if (types.length == 0) {
            lexer.nextToken(ModelToken.RBRACKET);

            if (lexer.token() != ModelToken.RBRACKET) {
                throw new ModelException("syntax error");
            }

            lexer.nextToken(ModelToken.COMMA);
            return new Object[0];
        }

        lexer.nextToken(ModelToken.LITERAL_INT);

        for (int i = 0; i < types.length; ++i) {
            Object value;

            if (lexer.token() == ModelToken.NULL) {
                value = null;
                lexer.nextToken(ModelToken.COMMA);
            } else {
                Type type = types[i];
                if (type == int.class || type == Integer.class) {
                    if (lexer.token() == ModelToken.LITERAL_INT) {
                        value = Integer.valueOf(lexer.intValue());
                        lexer.nextToken(ModelToken.COMMA);
                    } else {
                        value = this.parse();
                        value = TypeUtils.cast(value, type, config);
                    }
                } else if (type == String.class) {
                    if (lexer.token() == ModelToken.LITERAL_STRING) {
                        value = lexer.stringVal();
                        lexer.nextToken(ModelToken.COMMA);
                    } else {
                        value = this.parse();
                        value = TypeUtils.cast(value, type, config);
                    }
                } else {
                    boolean isArray = false;
                    Class<?> componentType = null;
                    if (i == types.length - 1) {
                        if (type instanceof Class) {
                            Class<?> clazz = (Class<?>) type;
                            isArray = clazz.isArray();
                            componentType = clazz.getComponentType();
                        }
                    }

                    // support varArgs
                    if (isArray && lexer.token() != ModelToken.LBRACKET) {
                        List<Object> varList = new ArrayList<Object>();

                        ObjectDeserializer derializer = config.getDeserializer(componentType);
                        int fastMatch = derializer.getFastMatchToken();

                        if (lexer.token() != ModelToken.RBRACKET) {
                            for (;;) {
                                Object item = derializer.deserialze(this, type, null);
                                varList.add(item);

                                if (lexer.token() == ModelToken.COMMA) {
                                    lexer.nextToken(fastMatch);
                                } else if (lexer.token() == ModelToken.RBRACKET) {
                                    break;
                                } else {
                                    throw new ModelException("syntax error :" + ModelToken.name(lexer.token()));
                                }
                            }
                        }

                        value = TypeUtils.cast(varList, type, config);
                    } else {
                        ObjectDeserializer derializer = config.getDeserializer(type);
                        value = derializer.deserialze(this, type, null);
                    }
                }
            }
            list[i] = value;

            if (lexer.token() == ModelToken.RBRACKET) {
                break;
            }

            if (lexer.token() != ModelToken.COMMA) {
                throw new ModelException("syntax error :" + ModelToken.name(lexer.token()));
            }

            if (i == types.length - 1) {
                lexer.nextToken(ModelToken.RBRACKET);
            } else {
                lexer.nextToken(ModelToken.LITERAL_INT);
            }
        }

        if (lexer.token() != ModelToken.RBRACKET) {
            throw new ModelException("syntax error");
        }

        lexer.nextToken(ModelToken.COMMA);

        return list;
    }

    public void parseObject(Object object) {
        Class<?> clazz = object.getClass();
        JavaBeanDeserializer beanDeser = null;
        ObjectDeserializer deserizer = config.getDeserializer(clazz);
        if (deserizer instanceof JavaBeanDeserializer) {
            beanDeser = (JavaBeanDeserializer) deserizer;
        }

        if (lexer.token() != ModelToken.LBRACE && lexer.token() != ModelToken.COMMA) {
            throw new ModelException("syntax error, expect {, actual " + lexer.tokenName());
        }

        for (;;) {
            // lexer.scanSymbol
            String key = lexer.scanSymbol(symbolTable);

            if (key == null) {
                if (lexer.token() == ModelToken.RBRACE) {
                    lexer.nextToken(ModelToken.COMMA);
                    break;
                }
                if (lexer.token() == ModelToken.COMMA) {
                    if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                        continue;
                    }
                }
            }

            FieldDeserializer fieldDeser = null;
            if (beanDeser != null) {
                fieldDeser = beanDeser.getFieldDeserializer(key);
            }
            
            if (fieldDeser == null) {
                if (!lexer.isEnabled(Feature.IgnoreNotMatch)) {
                    throw new ModelException("setter not found, class " + clazz.getName() + ", property " + key);
                }

                lexer.nextTokenWithColon();
                parse(); // skip

                if (lexer.token() == ModelToken.RBRACE) {
                    lexer.nextToken();
                    return;
                }

                continue;
            } else {
                Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
                Type fieldType = fieldDeser.fieldInfo.fieldType;
                Object fieldValue;
                if (fieldClass == int.class) {
                    lexer.nextTokenWithColon(ModelToken.LITERAL_INT);
                    fieldValue = IntegerCodec.instance.deserialze(this, fieldType, null);
                } else if (fieldClass == String.class) {
                    lexer.nextTokenWithColon(ModelToken.LITERAL_STRING);
                    fieldValue = StringCodec.deserialze(this);
                } else if (fieldClass == long.class) {
                    lexer.nextTokenWithColon(ModelToken.LITERAL_INT);
                    fieldValue = LongCodec.instance.deserialze(this, fieldType, null);
                } else {
                    ObjectDeserializer fieldValueDeserializer = config.getDeserializer(fieldClass, fieldType);

                    lexer.nextTokenWithColon(fieldValueDeserializer.getFastMatchToken());
                    fieldValue = fieldValueDeserializer.deserialze(this, fieldType, null);
                }

                fieldDeser.setValue(object, fieldValue);
            }

            if (lexer.token() == ModelToken.COMMA) {
                continue;
            }

            if (lexer.token() == ModelToken.RBRACE) {
                lexer.nextToken(ModelToken.COMMA);
                return;
            }
        }
    }

    public Object parseArrayWithType(Type collectionType) {
        if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken();
            return null;
        }

        Type[] actualTypes = ((ParameterizedType) collectionType).getActualTypeArguments();

        if (actualTypes.length != 1) {
            throw new ModelException("not support type " + collectionType);
        }

        Type actualTypeArgument = actualTypes[0];

        if (actualTypeArgument instanceof Class) {
            List<Object> array = new ArrayList<Object>();
            this.parseArray((Class<?>) actualTypeArgument, array);
            return array;
        }

        if (actualTypeArgument instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) actualTypeArgument;

            // assert wildcardType.getUpperBounds().length == 1;
            Type upperBoundType = wildcardType.getUpperBounds()[0];

            // assert upperBoundType instanceof Class;
            if (Object.class.equals(upperBoundType)) {
                if (wildcardType.getLowerBounds().length == 0) {
                    // Collection<?>
                    return parse();
                } else {
                    throw new ModelException("not support type : " + collectionType);
                }
            }

            List<Object> array = new ArrayList<Object>();
            this.parseArray((Class<?>) upperBoundType, array);
            return array;

            // throw new JSONException("not support type : " +
            // collectionType);return parse();
        }

        if (actualTypeArgument instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
            Type[] bounds = typeVariable.getBounds();

            if (bounds.length != 1) {
                throw new ModelException("not support : " + typeVariable);
            }

            Type boundType = bounds[0];
            if (boundType instanceof Class) {
                List<Object> array = new ArrayList<Object>();
                this.parseArray((Class<?>) boundType, array);
                return array;
            }
        }

        if (actualTypeArgument instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) actualTypeArgument;

            List<Object> array = new ArrayList<Object>();
            this.parseArray(parameterizedType, array);
            return array;
        }

        throw new ModelException("TODO : " + collectionType);
    }

    public void acceptType(String typeName) {
        ModelLexer lexer = this.lexer;

        lexer.nextTokenWithColon();

        if (lexer.token() != ModelToken.LITERAL_STRING) {
            throw new ModelException("type not match error");
        }

        if (typeName.equals(lexer.stringVal())) {
            lexer.nextToken();
            if (lexer.token() == ModelToken.COMMA) {
                lexer.nextToken();
            }
        } else {
            throw new ModelException("type not match error");
        }
    }

    public int getResolveStatus() {
        return resolveStatus;
    }

    public void setResolveStatus(int resolveStatus) {
        this.resolveStatus = resolveStatus;
    }

    public Object getObject(String path) {
        for (int i = 0; i < contextArrayIndex; ++i) {
            if (path.equals(contextArray[i].toString())) {
                return contextArray[i].object;
            }
        }

        return null;
    }

    @SuppressWarnings("rawtypes")
    public void checkListResolve(Collection array) {
        if (resolveStatus == NeedToResolve) {
            if (array instanceof List) {
                final int index = array.size() - 1;
                final List list = (List) array;
                ResolveTask task = getLastResolveTask();
                task.fieldDeserializer = new ResolveFieldDeserializer(this, list, index);
                task.ownerContext = context;
                setResolveStatus(DefaultModelParser.NONE);
            } else {
                ResolveTask task = getLastResolveTask();
                task.fieldDeserializer  = new ResolveFieldDeserializer(array);
                task.ownerContext = context;
                setResolveStatus(DefaultModelParser.NONE);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public void checkMapResolve(Map object, Object fieldName) {
        if (resolveStatus == NeedToResolve) {
            ResolveFieldDeserializer fieldResolver = new ResolveFieldDeserializer(object, fieldName);
            ResolveTask task = getLastResolveTask();
            task.fieldDeserializer = fieldResolver;
            task.ownerContext = context;
            setResolveStatus(DefaultModelParser.NONE);
        }
    }

    @SuppressWarnings("rawtypes")
    public Object parseObject(final Map object) {
        return parseObject(object, null);
    }

    public ModelObject parseObject() {
        ModelObject object = new ModelObject(lexer.isEnabled(Feature.OrderedField));
        object = (ModelObject) parseObject(object);
        return object;
    }

    @SuppressWarnings("rawtypes")
    public final void parseArray(final Collection array) {
        parseArray(array, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void parseArray(final Collection array, Object fieldName) {
        final ModelLexer lexer = this.lexer;

        if (lexer.token() == ModelToken.SET || lexer.token() == ModelToken.TREE_SET) {
            lexer.nextToken();
        }

        if (lexer.token() != ModelToken.LBRACKET) {
            throw new ModelException("syntax error, expect [, actual " + ModelToken.name(lexer.token()) + ", pos "
                                    + lexer.pos());
        }

        lexer.nextToken(ModelToken.LITERAL_STRING);

        ParseContext context = this.context;
        this.setContext(array, fieldName);
        try {
            for (int i = 0;; ++i) {
                if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (lexer.token() == ModelToken.COMMA) {
                        lexer.nextToken();
                        continue;
                    }
                }

                Object value;
                switch (lexer.token()) {
                    case LITERAL_INT:
                        value = lexer.integerValue();
                        lexer.nextToken(ModelToken.COMMA);
                        break;
                    case LITERAL_FLOAT:
                        if (lexer.isEnabled(Feature.UseBigDecimal)) {
                            value = lexer.decimalValue(true);
                        } else {
                            value = lexer.decimalValue(false);
                        }
                        lexer.nextToken(ModelToken.COMMA);
                        break;
                    case LITERAL_STRING:
                        String stringLiteral = lexer.stringVal();
                        lexer.nextToken(ModelToken.COMMA);

                        if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                            ModelScanner iso8601Lexer = new ModelScanner(stringLiteral);
                            if (iso8601Lexer.scanISO8601DateIfMatch()) {
                                value = iso8601Lexer.getCalendar().getTime();
                            } else {
                                value = stringLiteral;
                            }
                            iso8601Lexer.close();
                        } else {
                            value = stringLiteral;
                        }

                        break;
                    case TRUE:
                        value = Boolean.TRUE;
                        lexer.nextToken(ModelToken.COMMA);
                        break;
                    case FALSE:
                        value = Boolean.FALSE;
                        lexer.nextToken(ModelToken.COMMA);
                        break;
                    case LBRACE:
                        ModelObject object = new ModelObject(lexer.isEnabled(Feature.OrderedField));
                        value = parseObject(object, i);
                        break;
                    case LBRACKET:
                        Collection items = new ModelArray();
                        parseArray(items, i);
                        if (lexer.isEnabled(Feature.UseObjectArray)) {
                            value = items.toArray();
                        } else {
                            value = items;
                        }
                        break;
                    case NULL:
                        value = null;
                        lexer.nextToken(ModelToken.LITERAL_STRING);
                        break;
                    case UNDEFINED:
                        value = null;
                        lexer.nextToken(ModelToken.LITERAL_STRING);
                        break;
                    case RBRACKET:
                        lexer.nextToken(ModelToken.COMMA);
                        return;
                    case EOF:
                        throw new ModelException("unclosed jsonArray");
                    default:
                        value = parse();
                        break;
                }

                array.add(value);
                checkListResolve(array);

                if (lexer.token() == ModelToken.COMMA) {
                    lexer.nextToken(ModelToken.LITERAL_STRING);
                    continue;
                }
            }
        } finally {
            this.setContext(context);
        }
    }

    public ParseContext getContext() {
        return context;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (resolveTaskList == null) {
            resolveTaskList = new ArrayList<ResolveTask>(2);
        }
        return resolveTaskList;
    }

    public void addResolveTask(ResolveTask task) {
        if (resolveTaskList == null) {
            resolveTaskList = new ArrayList<ResolveTask>(2);
        }
        resolveTaskList.add(task);
    }

    public ResolveTask getLastResolveTask() {
        return resolveTaskList.get(resolveTaskList.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (extraProcessors == null) {
            extraProcessors = new ArrayList<ExtraProcessor>(2);
        }
        return extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (extraTypeProviders == null) {
            extraTypeProviders = new ArrayList<ExtraTypeProvider>(2);
        }
        return extraTypeProviders;
    }

    public FieldTypeResolver getFieldTypeResolver() {
        return fieldTypeResolver;
    }
    
    public void setFieldTypeResolver(FieldTypeResolver fieldTypeResolver) {
        this.fieldTypeResolver = fieldTypeResolver;
    }

    public void setContext(ParseContext context) {
        if (lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return;
        }
        this.context = context;
    }

    public void popContext() {
        if (lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return;
        }

        this.context = this.context.parent;
        contextArray[contextArrayIndex - 1] = null;
        contextArrayIndex--;
    }

    public ParseContext setContext(Object object, Object fieldName) {
        if (lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }

        return setContext(this.context, object, fieldName);
    }

    public ParseContext setContext(ParseContext parent, Object object, Object fieldName) {
        if (lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }

        this.context = new ParseContext(parent, object, fieldName);
        addContext(this.context);

        return this.context;
    }

    private void addContext(ParseContext context) {
        int i = contextArrayIndex++;
        if (contextArray == null) {
            contextArray = new ParseContext[8];
        } else if (i >= contextArray.length) {
            int newLen = (contextArray.length * 3) / 2;
            ParseContext[] newArray = new ParseContext[newLen];
            System.arraycopy(contextArray, 0, newArray, 0, contextArray.length);
            contextArray = newArray;
        }
        contextArray[i] = context;
    }

    public Object parse() {
        return parse(null);
    }

    public Object parseKey() {
        if (lexer.token() == ModelToken.IDENTIFIER) {
            String value = lexer.stringVal();
            lexer.nextToken(ModelToken.COMMA);
            return value;
        }
        return parse(null);
    }

    public Object parse(Object fieldName) {
        final ModelLexer lexer = this.lexer;
        switch (lexer.token()) {
            case SET:
                lexer.nextToken();
                HashSet<Object> set = new HashSet<Object>();
                parseArray(set, fieldName);
                return set;
            case TREE_SET:
                lexer.nextToken();
                TreeSet<Object> treeSet = new TreeSet<Object>();
                parseArray(treeSet, fieldName);
                return treeSet;
            case LBRACKET:
                ModelArray array = new ModelArray();
                parseArray(array, fieldName);
                if (lexer.isEnabled(Feature.UseObjectArray)) {
                    return array.toArray();
                }
                return array;
            case LBRACE:
                ModelObject object = new ModelObject(lexer.isEnabled(Feature.OrderedField));
                return parseObject(object, fieldName);
            case LITERAL_INT:
                Number intValue = lexer.integerValue();
                lexer.nextToken();
                return intValue;
            case LITERAL_FLOAT:
                Object value = lexer.decimalValue(lexer.isEnabled(Feature.UseBigDecimal));
                lexer.nextToken();
                return value;
            case LITERAL_STRING:
                String stringLiteral = lexer.stringVal();
                lexer.nextToken(ModelToken.COMMA);

                if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    ModelScanner iso8601Lexer = new ModelScanner(stringLiteral);
                    try {
                        if (iso8601Lexer.scanISO8601DateIfMatch()) {
                            return iso8601Lexer.getCalendar().getTime();
                        }
                    } finally {
                        iso8601Lexer.close();
                    }
                }

                return stringLiteral;
            case NULL:
                lexer.nextToken();
                return null;
            case UNDEFINED:
                lexer.nextToken();
                return null;
            case TRUE:
                lexer.nextToken();
                return Boolean.TRUE;
            case FALSE:
                lexer.nextToken();
                return Boolean.FALSE;
            case NEW:
                lexer.nextToken(ModelToken.IDENTIFIER);

                if (lexer.token() != ModelToken.IDENTIFIER) {
                    throw new ModelException("syntax error");
                }
                lexer.nextToken(ModelToken.LPAREN);

                accept(ModelToken.LPAREN);
                long time = ((Number) lexer.integerValue()).longValue();
                accept(ModelToken.LITERAL_INT);

                accept(ModelToken.RPAREN);

                return new Date(time);
            case EOF:
                if (lexer.isBlankInput()) {
                    return null;
                }
                throw new ModelException("unterminated json string, " + lexer.info());
            case ERROR:
            default:
                throw new ModelException("syntax error, " + lexer.info());
        }
    }

    public void config(Feature feature, boolean state) {
        this.lexer.config(feature, state);
    }

    public boolean isEnabled(Feature feature) {
        return lexer.isEnabled(feature);
    }

    public ModelLexer getLexer() {
        return lexer;
    }

    public final void accept(final int token) {
        final ModelLexer lexer = this.lexer;
        if (lexer.token() == token) {
            lexer.nextToken();
        } else {
            throw new ModelException("syntax error, expect " + ModelToken.name(token) + ", actual "
                                    + ModelToken.name(lexer.token()));
        }
    }

    public final void accept(final int token, int nextExpectToken) {
        final ModelLexer lexer = this.lexer;
        if (lexer.token() == token) {
            lexer.nextToken(nextExpectToken);
        } else {
            throwException(token);
        }
    }
    
    public void throwException(int token) {
        throw new ModelException("syntax error, expect " + ModelToken.name(token) + ", actual "
                                + ModelToken.name(lexer.token()));
    }
    
    public void close() {
        final ModelLexer lexer = this.lexer;

        try {
            if (lexer.isEnabled(Feature.AutoCloseSource)) {
                if (lexer.token() != ModelToken.EOF) {
                    throw new ModelException("not close json text, token : " + ModelToken.name(lexer.token()));
                }
            }
        } finally {
            lexer.close();
        }
    }

    public void handleResovleTask(Object value) {
        if (resolveTaskList == null) {
            return;
        }

        int size = resolveTaskList.size();
        for (int i = 0; i < size; ++i) {
            ResolveTask task = resolveTaskList.get(i);
            String ref = task.referenceValue;

            Object object = null;
            if (task.ownerContext != null) {
                object = task.ownerContext.object;
            }

            Object refValue;
            if (ref.startsWith("$")) {
                refValue = getObject(ref);
            } else {
                refValue = task.context.object;
            }
            
            FieldDeserializer fieldDeser = task.fieldDeserializer;

            if (fieldDeser != null) {
                fieldDeser.setValue(object, refValue);
            }
        }
    }

    public static class ResolveTask {

        public final ParseContext context;
        public final String       referenceValue;
        public FieldDeserializer  fieldDeserializer;
        public ParseContext       ownerContext;

        public ResolveTask(ParseContext context, String referenceValue){
            this.context = context;
            this.referenceValue = referenceValue;
        }
    }
    
    public void parseExtra(Object object, String key) {
        final ModelLexer lexer = this.lexer; // xxx
        lexer.nextTokenWithColon();
        Type type = null;
        
        if (extraTypeProviders != null) {
            for (ExtraTypeProvider extraProvider : extraTypeProviders) {
                type = extraProvider.getExtraType(object, key);
            }
        }
        Object value = type == null //
            ? parse() // skip
            : parseObject(type);
            
        if (object instanceof ExtraProcessable) {
            ExtraProcessable extraProcessable = ((ExtraProcessable) object);
            extraProcessable.processExtra(key, value);
            return;
        }

        if (extraProcessors != null) {
            for (ExtraProcessor process : extraProcessors) {
                process.processExtra(object, key, value);
            }
        }
    }
}
