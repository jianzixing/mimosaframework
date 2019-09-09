package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.Type;

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.ModelToken;

@SuppressWarnings("rawtypes")
public class EnumDeserializer implements ObjectDeserializer {

    private final Class<?> enumClass;
    protected final Enum[]   values;

    public EnumDeserializer(Class<?> enumClass){
        this.enumClass = enumClass;
        values = (Enum[]) enumClass.getEnumConstants();
    }
    
    public Enum<?> valueOf(int ordinal) {
        return values[ordinal];
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type type, Object fieldName) {
        try {
            Object value;
            final ModelLexer lexer = parser.lexer;
            final int token = lexer.token();
            if (token == ModelToken.LITERAL_INT) {
                int intValue = lexer.intValue();
                lexer.nextToken(ModelToken.COMMA);

                if (intValue < 0 || intValue > values.length) {
                    throw new ModelException("parse enum " + enumClass.getName() + " error, value : " + intValue);
                }

                return (T) values[intValue];
            } else if (token == ModelToken.LITERAL_STRING) {
                String strVal = lexer.stringVal();
                lexer.nextToken(ModelToken.COMMA);

                if (strVal.length() == 0) {
                    return (T) null;
                }

                return (T) Enum.valueOf((Class<Enum>) enumClass, strVal);
            } else if (token == ModelToken.NULL) {
                value = null;
                lexer.nextToken(ModelToken.COMMA);

                return null;
            } else {
                value = parser.parse();
            }

            throw new ModelException("parse enum " + enumClass.getName() + " error, value : " + value);
        } catch (ModelException e) {
            throw e;
        } catch (Exception e) {
            throw new ModelException(e.getMessage(), e);
        }
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_INT;
    }
}
