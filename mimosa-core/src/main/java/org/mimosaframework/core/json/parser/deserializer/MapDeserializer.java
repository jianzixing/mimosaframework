package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.parser.ParseContext;
import org.mimosaframework.core.json.util.TypeUtils;

public class MapDeserializer implements ObjectDeserializer {
    public static MapDeserializer instance = new MapDeserializer();
    
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type type, Object fieldName) {
        if (type == ModelObject.class && parser.getFieldTypeResolver() == null) {
            return (T) parser.parseObject();
        }
        
        final ModelLexer lexer = parser.lexer;
        if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken(ModelToken.COMMA);
            return null;
        }

        Map<Object, Object> map = createMap(type);

        ParseContext context = parser.getContext();

        try {
            parser.setContext(context, map, fieldName);
            return (T) deserialze(parser, type, fieldName, map);
        } finally {
            parser.setContext(context);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Object deserialze(DefaultModelParser parser, Type type, Object fieldName, Map map) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type keyType = parameterizedType.getActualTypeArguments()[0];
            Type valueType = parameterizedType.getActualTypeArguments()[1];

            if (String.class == keyType) {
                return parseMap(parser, (Map<String, Object>) map, valueType, fieldName);
            } else {
                return parseMap(parser, map, keyType, valueType, fieldName);
            }
        } else {
            return parser.parseObject(map, fieldName);
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static Map parseMap(DefaultModelParser parser, Map<String, Object> map, Type valueType, Object fieldName) {
        ModelLexer lexer = parser.lexer;

        if (lexer.token() != ModelToken.LBRACE) {
            throw new ModelException("syntax error, expect {, actual " + lexer.token());
        }

        ParseContext context = parser.getContext();
        try {
            for (int i = 0;;++i) {
                lexer.skipWhitespace();
                char ch = lexer.getCurrent();
                if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (ch == ',') {
                        lexer.next();
                        lexer.skipWhitespace();
                        ch = lexer.getCurrent();
                    }
                }

                String key;
                if (ch == '"') {
                    key = lexer.scanSymbol(parser.getSymbolTable(), '"');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new ModelException("expect ':' at " + lexer.pos());
                    }
                } else if (ch == '}') {
                    lexer.next();
                    lexer.resetStringPosition();
                    lexer.nextToken(ModelToken.COMMA);
                    return map;
                } else if (ch == '\'') {
                    if (!lexer.isEnabled(Feature.AllowSingleQuotes)) {
                        throw new ModelException("syntax error");
                    }

                    key = lexer.scanSymbol(parser.getSymbolTable(), '\'');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new ModelException("expect ':' at " + lexer.pos());
                    }
                } else {
                    if (!lexer.isEnabled(Feature.AllowUnQuotedFieldNames)) {
                        throw new ModelException("syntax error");
                    }

                    key = lexer.scanSymbolUnQuoted(parser.getSymbolTable());
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new ModelException("expect ':' at " + lexer.pos() + ", actual " + ch);
                    }
                }

                lexer.next();
                lexer.skipWhitespace();
                ch = lexer.getCurrent();

                lexer.resetStringPosition();

                if (key == Model.DEFAULT_TYPE_KEY && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    String typeName = lexer.scanSymbol(parser.getSymbolTable(), '"');
                    Class<?> clazz = TypeUtils.loadClass(typeName, parser.getConfig().getDefaultClassLoader());

                    if (Map.class.isAssignableFrom(clazz) ) {
                        lexer.nextToken(ModelToken.COMMA);
                        if (lexer.token() == ModelToken.RBRACE) {
                            lexer.nextToken(ModelToken.COMMA);
                            return map;
                        }
                        continue;
                    }

                    ObjectDeserializer deserializer = parser.getConfig().getDeserializer(clazz);

                    lexer.nextToken(ModelToken.COMMA);

                    parser.setResolveStatus(DefaultModelParser.TypeNameRedirect);

                    if (context != null && !(fieldName instanceof Integer)) {
                        parser.popContext();
                    }

                    return (Map) deserializer.deserialze(parser, clazz, fieldName);
                }

                Object value;
                lexer.nextToken();

                if (i != 0) {
                    parser.setContext(context);
                }
                
                if (lexer.token() == ModelToken.NULL) {
                    value = null;
                    lexer.nextToken();
                } else {
                    value = parser.parseObject(valueType, key);
                }

                map.put(key, value);
                parser.checkMapResolve(map, key);

                parser.setContext(context, value, key);
                parser.setContext(context);

                final int tok = lexer.token();
                if (tok == ModelToken.EOF || tok == ModelToken.RBRACKET) {
                    return map;
                }

                if (tok == ModelToken.RBRACE) {
                    lexer.nextToken();
                    return map;
                }
            }
        } finally {
            parser.setContext(context);
        }

    }
    
    public static Object parseMap(DefaultModelParser parser, Map<Object, Object> map, Type keyType, Type valueType,
                                  Object fieldName) {
        ModelLexer lexer = parser.lexer;

        if (lexer.token() != ModelToken.LBRACE && lexer.token() != ModelToken.COMMA) {
            throw new ModelException("syntax error, expect {, actual " + lexer.tokenName());
        }

        ObjectDeserializer keyDeserializer = parser.getConfig().getDeserializer(keyType);
        ObjectDeserializer valueDeserializer = parser.getConfig().getDeserializer(valueType);
        lexer.nextToken(keyDeserializer.getFastMatchToken());

        ParseContext context = parser.getContext();
        try {
            for (;;) {
                if (lexer.token() == ModelToken.RBRACE) {
                    lexer.nextToken(ModelToken.COMMA);
                    break;
                }

                if (lexer.token() == ModelToken.LITERAL_STRING //
                    && lexer.isRef() //
                    && !lexer.isEnabled(Feature.DisableSpecialKeyDetect) //
                ) {
                    Object object = null;

                    lexer.nextTokenWithColon(ModelToken.LITERAL_STRING);
                    if (lexer.token() == ModelToken.LITERAL_STRING) {
                        String ref = lexer.stringVal();
                        if ("..".equals(ref)) {
                            ParseContext parentContext = context.parent;
                            object = parentContext.object;
                        } else if ("$".equals(ref)) {
                            ParseContext rootContext = context;
                            while (rootContext.parent != null) {
                                rootContext = rootContext.parent;
                            }

                            object = rootContext.object;
                        } else {
                            parser.addResolveTask(new DefaultModelParser.ResolveTask(context, ref));
                            parser.setResolveStatus(DefaultModelParser.NeedToResolve);
                        }
                    } else {
                        throw new ModelException("illegal ref, " + ModelToken.name(lexer.token()));
                    }

                    lexer.nextToken(ModelToken.RBRACE);
                    if (lexer.token() != ModelToken.RBRACE) {
                        throw new ModelException("illegal ref");
                    }
                    lexer.nextToken(ModelToken.COMMA);

                    // parser.setContext(context, map, fieldName);
                    // parser.setContext(context);

                    return object;
                }

                if (map.size() == 0 //
                    && lexer.token() == ModelToken.LITERAL_STRING //
                    && Model.DEFAULT_TYPE_KEY.equals(lexer.stringVal()) //
                    && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    lexer.nextTokenWithColon(ModelToken.LITERAL_STRING);
                    lexer.nextToken(ModelToken.COMMA);
                    if (lexer.token() == ModelToken.RBRACE) {
                        lexer.nextToken();
                        return map;
                    }
                    lexer.nextToken(keyDeserializer.getFastMatchToken());
                }

                Object key = keyDeserializer.deserialze(parser, keyType, null);

                if (lexer.token() != ModelToken.COLON) {
                    throw new ModelException("syntax error, expect :, actual " + lexer.token());
                }

                lexer.nextToken(valueDeserializer.getFastMatchToken());

                Object value = valueDeserializer.deserialze(parser, valueType, key);
                parser.checkMapResolve(map, key);

                map.put(key, value);

                if (lexer.token() == ModelToken.COMMA) {
                    lexer.nextToken(keyDeserializer.getFastMatchToken());
                }
            }
        } finally {
            parser.setContext(context);
        }

        return map;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Map<Object, Object> createMap(Type type) {
        if (type == Properties.class) {
            return new Properties();
        }

        if (type == Hashtable.class) {
            return new Hashtable();
        }

        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }

        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }

        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        
        if (type == Map.class || type == HashMap.class) {
            return new HashMap();
        }
        
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            return createMap(parameterizedType.getRawType());
        }

        Class<?> clazz = (Class<?>) type;
        if (clazz.isInterface()) {
            throw new ModelException("unsupport type " + type);
        }
        
        try {
            return (Map<Object, Object>) clazz.newInstance();
        } catch (Exception e) {
            throw new ModelException("unsupport type " + type, e);
        }
    }
    

    public int getFastMatchToken() {
        return ModelToken.LBRACE;
    }
}
