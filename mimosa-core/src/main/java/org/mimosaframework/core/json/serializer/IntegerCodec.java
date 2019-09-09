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
package org.mimosaframework.core.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class IntegerCodec implements ObjectSerializer, ObjectDeserializer {

    public static IntegerCodec instance = new IntegerCodec();

    public void write(ModelSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        Number value = (Number) object;
        
        if (value == null) {
            out.writeNull(SerializerFeature.WriteNullNumberAsZero);
            return;
        }
        
        if (object instanceof Long) {
            out.writeLong(value.longValue());
        } else {
            out.writeInt(value.intValue());
        }
        
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            Class<?> clazz = value.getClass();
            if (clazz == Byte.class) {
                out.write('B');
            } else if (clazz == Short.class) {
                out.write('S');
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName) {
        final ModelLexer lexer = parser.lexer;

        if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken(ModelToken.COMMA);
            return null;
        }

        Integer intObj;
        if (lexer.token() == ModelToken.LITERAL_INT) {
            int val;
            try {
                 val = lexer.intValue();
            } catch (NumberFormatException ex) {
                throw new ModelException("int value overflow, field : " + fieldName, ex);
            }
            lexer.nextToken(ModelToken.COMMA);
            intObj = Integer.valueOf(val);
        } else if (lexer.token() == ModelToken.LITERAL_FLOAT) {
            BigDecimal decimalValue = lexer.decimalValue();
            lexer.nextToken(ModelToken.COMMA);
            intObj = Integer.valueOf(decimalValue.intValue());
        } else {
            Object value = parser.parse();

            intObj = TypeUtils.castToInt(value);
        }
        
        if (clazz == AtomicInteger.class) {
            return (T) new AtomicInteger(intObj.intValue());
        }
        
        return (T) intObj;
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_INT;
    }
}
