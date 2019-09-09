package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.Type;

import org.mimosaframework.core.json.parser.DefaultModelParser;

public abstract class ContextObjectDeserializer implements ObjectDeserializer {
    public <T> T deserialze(DefaultModelParser parser, Type type, Object fieldName) {
        return deserialze(parser, type, fieldName, null, 0);
    }
    
    public abstract <T> T deserialze(DefaultModelParser parser, Type type, Object fieldName, String format, int features);
}
