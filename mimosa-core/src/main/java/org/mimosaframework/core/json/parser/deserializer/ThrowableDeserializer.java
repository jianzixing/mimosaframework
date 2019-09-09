package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.parser.ParserConfig;
import org.mimosaframework.core.json.util.TypeUtils;

public class ThrowableDeserializer extends JavaBeanDeserializer {

    public ThrowableDeserializer(ParserConfig mapping, Class<?> clazz){
        super(mapping, clazz, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type type, Object fieldName) {
        ModelLexer lexer = parser.lexer;
        
        if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (parser.getResolveStatus() == DefaultModelParser.TypeNameRedirect) {
            parser.setResolveStatus(DefaultModelParser.NONE);
        } else {
            if (lexer.token() != ModelToken.LBRACE) {
                throw new ModelException("syntax error");
            }
        }

        Throwable cause = null;
        Class<?> exClass = null;
        
        if (type != null && type instanceof Class) {
        	Class<?> clazz = (Class<?>) type;
        	if (Throwable.class.isAssignableFrom(clazz)) {
        		exClass = clazz;
        	}
        }
        
        String message = null;
        StackTraceElement[] stackTrace = null;
        Map<String, Object> otherValues = new HashMap<String, Object>();

        for (;;) {
            // lexer.scanSymbol
            String key = lexer.scanSymbol(parser.getSymbolTable());

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

            lexer.nextTokenWithColon(ModelToken.LITERAL_STRING);

            if (Model.DEFAULT_TYPE_KEY.equals(key)) {
                if (lexer.token() == ModelToken.LITERAL_STRING) {
                    String exClassName = lexer.stringVal();
                    exClass = TypeUtils.loadClass(exClassName, parser.getConfig().getDefaultClassLoader());
                } else {
                    throw new ModelException("syntax error");
                }
                lexer.nextToken(ModelToken.COMMA);
            } else if ("message".equals(key)) {
                if (lexer.token() == ModelToken.NULL) {
                    message = null;
                } else if (lexer.token() == ModelToken.LITERAL_STRING) {
                    message = lexer.stringVal();
                } else {
                    throw new ModelException("syntax error");
                }
                lexer.nextToken();
            } else if ("cause".equals(key)) {
                cause = deserialze(parser, null, "cause");
            } else if ("stackTrace".equals(key)) {
                stackTrace = parser.parseObject(StackTraceElement[].class);
            } else {
                // TODO
                otherValues.put(key, parser.parse());
            }

            if (lexer.token() == ModelToken.RBRACE) {
                lexer.nextToken(ModelToken.COMMA);
                break;
            }
        }

        Throwable ex = null;
        if (exClass == null) {
            ex = new Exception(message, cause);
        } else {
            try {
                ex = createException(message, cause, exClass);
                if (ex == null) {
                    ex = new Exception(message, cause);
                }
            } catch (Exception e) {
                throw new ModelException("create instance error", e);
            }
        }

        if (stackTrace != null) {
            ex.setStackTrace(stackTrace);
        }

        return (T) ex;
    }

    private Throwable createException(String message, Throwable cause, Class<?> exClass) throws Exception {
        Constructor<?> defaultConstructor = null;
        Constructor<?> messageConstructor = null;
        Constructor<?> causeConstructor = null;
        for (Constructor<?> constructor : exClass.getConstructors()) {
        	Class<?>[] types = constructor.getParameterTypes();
            if (types.length == 0) {
                defaultConstructor = constructor;
                continue;
            }

            if (types.length == 1 && types[0] == String.class) {
                messageConstructor = constructor;
                continue;
            }

            if (types.length == 2 && types[0] == String.class && types[1] == Throwable.class) {
                causeConstructor = constructor;
                continue;
            }
        }

        if (causeConstructor != null) {
            return (Throwable) causeConstructor.newInstance(message, cause);
        }

        if (messageConstructor != null) {
            return (Throwable) messageConstructor.newInstance(message);
        }

        if (defaultConstructor != null) {
            return (Throwable) defaultConstructor.newInstance();
        }

        return null;
    }

    public int getFastMatchToken() {
        return ModelToken.LBRACE;
    }
}
