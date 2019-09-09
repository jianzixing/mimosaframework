package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.Type;

import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.core.json.parser.ModelToken;

public class StackTraceElementDeserializer implements ObjectDeserializer {

    public final static StackTraceElementDeserializer instance = new StackTraceElementDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type type, Object fieldName) {
        ModelLexer lexer = parser.lexer;
        if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (lexer.token() != ModelToken.LBRACE && lexer.token() != ModelToken.COMMA) {
            throw new ModelException("syntax error: " + ModelToken.name(lexer.token()));
        }

        String declaringClass = null;
        String methodName = null;
        String fileName = null;
        int lineNumber = 0;

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
            if ("className".equals(key)) {
                if (lexer.token() == ModelToken.NULL) {
                    declaringClass = null;
                } else if (lexer.token() == ModelToken.LITERAL_STRING) {
                    declaringClass = lexer.stringVal();
                } else {
                    throw new ModelException("syntax error");
                }
            } else if ("methodName".equals(key)) {
                if (lexer.token() == ModelToken.NULL) {
                    methodName = null;
                } else if (lexer.token() == ModelToken.LITERAL_STRING) {
                    methodName = lexer.stringVal();
                } else {
                    throw new ModelException("syntax error");
                }
            } else if ("fileName".equals(key)) {
                if (lexer.token() == ModelToken.NULL) {
                    fileName = null;
                } else if (lexer.token() == ModelToken.LITERAL_STRING) {
                    fileName = lexer.stringVal();
                } else {
                    throw new ModelException("syntax error");
                }
            } else if ("lineNumber".equals(key)) {
                if (lexer.token() == ModelToken.NULL) {
                    lineNumber = 0;
                } else if (lexer.token() == ModelToken.LITERAL_INT) {
                    lineNumber = lexer.intValue();
                } else {
                    throw new ModelException("syntax error");
                }
            } else if ("nativeMethod".equals(key)) {
                if (lexer.token() == ModelToken.NULL) {
                    lexer.nextToken(ModelToken.COMMA);
                } else if (lexer.token() == ModelToken.TRUE) {
                    lexer.nextToken(ModelToken.COMMA);
                } else if (lexer.token() == ModelToken.FALSE) {
                    lexer.nextToken(ModelToken.COMMA);
                } else {
                    throw new ModelException("syntax error");
                }
            } else if (key == Model.DEFAULT_TYPE_KEY) {
               if (lexer.token() == ModelToken.LITERAL_STRING) {
                    String elementType = lexer.stringVal();
                    if (!elementType.equals("java.lang.StackTraceElement")) {
                        throw new ModelException("syntax error : " + elementType);
                    }
                } else {
                    if (lexer.token() != ModelToken.NULL) {
                        throw new ModelException("syntax error");
                    }
                }
            } else {
                throw new ModelException("syntax error : " + key);
            }

            if (lexer.token() == ModelToken.RBRACE) {
                lexer.nextToken(ModelToken.COMMA);
                break;
            }
        }
        return (T) new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
    }

    public int getFastMatchToken() {
        return ModelToken.LBRACE;
    }
}
