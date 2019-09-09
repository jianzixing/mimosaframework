package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.ModelScanner;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.util.TypeUtils;

public abstract class AbstractDateDeserializer extends ContextObjectDeserializer implements ObjectDeserializer {

    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName) {
        return deserialze(parser, clazz, fieldName, null, 0);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName, String format, int features) {
        ModelLexer lexer = parser.lexer;

        Object val;
        if (lexer.token() == ModelToken.LITERAL_INT) {
            val = lexer.longValue();
            lexer.nextToken(ModelToken.COMMA);
        } else if (lexer.token() == ModelToken.LITERAL_STRING) {
            String strVal = lexer.stringVal();
            
            if (format != null) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                    val = simpleDateFormat.parse(strVal);
                } catch (ParseException ex) {
                    // skip
                    val = null;
                }
            } else {
                val = null;
            }
            
            if (val == null) {
                val = strVal;
                lexer.nextToken(ModelToken.COMMA);
                
                if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    ModelScanner iso8601Lexer = new ModelScanner(strVal);
                    if (iso8601Lexer.scanISO8601DateIfMatch()) {
                        val = iso8601Lexer.getCalendar().getTime();
                    }
                    iso8601Lexer.close();
                }
            }
        } else if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken();
            val = null;
        } else if (lexer.token() == ModelToken.LBRACE) {
            lexer.nextToken();
            
            String key;
            if (lexer.token() == ModelToken.LITERAL_STRING) {
                key = lexer.stringVal();
                
                if (Model.DEFAULT_TYPE_KEY.equals(key)) {
                    lexer.nextToken();
                    parser.accept(ModelToken.COLON);
                    
                    String typeName = lexer.stringVal();
                    Class<?> type = TypeUtils.loadClass(typeName, parser.getConfig().getDefaultClassLoader());
                    if (type != null) {
                        clazz = type;
                    }
                    
                    parser.accept(ModelToken.LITERAL_STRING);
                    parser.accept(ModelToken.COMMA);
                }
                
                lexer.nextTokenWithColon(ModelToken.LITERAL_INT);
            } else {
                throw new ModelException("syntax error");
            }
            
            long timeMillis;
            if (lexer.token() == ModelToken.LITERAL_INT) {
                timeMillis = lexer.longValue();
                lexer.nextToken();
            } else {
                throw new ModelException("syntax error : " + lexer.tokenName());
            }
            
            val = timeMillis;
            
            parser.accept(ModelToken.RBRACE);
        } else if (parser.getResolveStatus() == DefaultModelParser.TypeNameRedirect) {
            parser.setResolveStatus(DefaultModelParser.NONE);
            parser.accept(ModelToken.COMMA);

            if (lexer.token() == ModelToken.LITERAL_STRING) {
                if (!"val".equals(lexer.stringVal())) {
                    throw new ModelException("syntax error");
                }
                lexer.nextToken();
            } else {
                throw new ModelException("syntax error");
            }

            parser.accept(ModelToken.COLON);

            val = parser.parse();

            parser.accept(ModelToken.RBRACE);
        } else {
            val = parser.parse();
        }

        return (T) cast(parser, clazz, fieldName, val);
    }

    protected abstract <T> T cast(DefaultModelParser parser, Type clazz, Object fieldName, Object value);
}
