package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.Type;

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.ModelScanner;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.ModelToken;

public class TimeDeserializer implements ObjectDeserializer {

    public final static TimeDeserializer instance = new TimeDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName) {
        ModelLexer lexer = parser.lexer;
        
        if (lexer.token() == ModelToken.COMMA) {
            lexer.nextToken(ModelToken.LITERAL_STRING);
            
            if (lexer.token() != ModelToken.LITERAL_STRING) {
                throw new ModelException("syntax error");
            }
            
            lexer.nextTokenWithColon(ModelToken.LITERAL_INT);
            
            if (lexer.token() != ModelToken.LITERAL_INT) {
                throw new ModelException("syntax error");
            }
            
            long time = lexer.longValue();
            lexer.nextToken(ModelToken.RBRACE);
            if (lexer.token() != ModelToken.RBRACE) {
                throw new ModelException("syntax error");
            }
            lexer.nextToken(ModelToken.COMMA);
            
            return (T) new java.sql.Time(time);
        }
        
        Object val = parser.parse();

        if (val == null) {
            return null;
        }

        if (val instanceof java.sql.Time) {
            return (T) val;
        } else if (val instanceof Number) {
            return (T) new java.sql.Time(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            
            long longVal;
            ModelScanner dateLexer = new ModelScanner(strVal);
            if (dateLexer.scanISO8601DateIfMatch()) {
                longVal = dateLexer.getCalendar().getTimeInMillis();
            } else {
                boolean isDigit = true;
                for (int i = 0; i< strVal.length(); ++i) {
                    char ch = strVal.charAt(i);
                    if (ch < '0' || ch > '9') {
                        isDigit = false;
                        break;
                    }
                }
                if (!isDigit) {
                    dateLexer.close();
                    return (T) java.sql.Time.valueOf(strVal);    
                }
                
                longVal = Long.parseLong(strVal);
            }
            dateLexer.close();
            return (T) new java.sql.Time(longVal);
        }
        
        throw new ModelException("parse error");
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_INT;
    }
}
