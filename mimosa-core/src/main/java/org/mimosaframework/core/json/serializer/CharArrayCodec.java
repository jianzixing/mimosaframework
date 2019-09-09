package org.mimosaframework.core.json.serializer;

import java.lang.reflect.Type;

import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.parser.ModelToken;


public class CharArrayCodec implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultModelParser parser) {
        final ModelLexer lexer = parser.lexer;
        if (lexer.token() == ModelToken.LITERAL_STRING) {
            String val = lexer.stringVal();
            lexer.nextToken(ModelToken.COMMA);
            return (T) val.toCharArray();
        }
        
        if (lexer.token() == ModelToken.LITERAL_INT) {
            Number val = lexer.integerValue();
            lexer.nextToken(ModelToken.COMMA);
            return (T) val.toString().toCharArray();
        }

        Object value = parser.parse();
        return value == null //
            ? null //
            : (T) Model.toJSONString(value).toCharArray();
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_STRING;
    }
}
