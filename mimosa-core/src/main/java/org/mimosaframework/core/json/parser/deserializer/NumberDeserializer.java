package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.util.TypeUtils;

public class NumberDeserializer implements ObjectDeserializer {

    public final static NumberDeserializer instance = new NumberDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName) {
        final ModelLexer lexer = parser.lexer;
        if (lexer.token() == ModelToken.LITERAL_INT) {
            if (clazz == double.class || clazz  == Double.class) {
                String val = lexer.numberString();
                lexer.nextToken(ModelToken.COMMA);
                return (T) Double.valueOf(Double.parseDouble(val));
            }
            
            long val = lexer.longValue();
            lexer.nextToken(ModelToken.COMMA);

            if (clazz == short.class || clazz == Short.class) {
                return (T) Short.valueOf((short) val);
            }

            if (clazz == byte.class || clazz == Byte.class) {
                return (T) Byte.valueOf((byte) val);
            }

            if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
                return (T) Integer.valueOf((int) val);
            }
            return (T) Long.valueOf(val);
        }

        if (lexer.token() == ModelToken.LITERAL_FLOAT) {
            if (clazz == double.class || clazz == Double.class) {
                String val = lexer.numberString();
                lexer.nextToken(ModelToken.COMMA);
                return (T) Double.valueOf(Double.parseDouble(val));
            }

            BigDecimal val = lexer.decimalValue();
            lexer.nextToken(ModelToken.COMMA);

            if (clazz == short.class || clazz == Short.class) {
                return (T) Short.valueOf(val.shortValue());
            }

            if (clazz == byte.class || clazz == Byte.class) {
                return (T) Byte.valueOf(val.byteValue());
            }

            return (T) val;
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        if (clazz == double.class || clazz == Double.class) {
            return (T) TypeUtils.castToDouble(value);
        }

        if (clazz == short.class || clazz == Short.class) {
            return (T) TypeUtils.castToShort(value);
        }

        if (clazz == byte.class || clazz == Byte.class) {
            return (T) TypeUtils.castToByte(value);
        }

        return (T) TypeUtils.castToBigDecimal(value);
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_INT;
    }
}
