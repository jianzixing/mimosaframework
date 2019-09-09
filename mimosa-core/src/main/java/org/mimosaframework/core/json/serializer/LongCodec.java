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
import java.util.concurrent.atomic.AtomicLong;

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class LongCodec implements ObjectSerializer, ObjectDeserializer {

    public static LongCodec instance = new LongCodec();

    public void write(ModelSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullNumberAsZero);
        } else {
            long value = ((Long) object).longValue();
            out.writeLong(value);
    
            if (out.isEnabled(SerializerFeature.WriteClassName) //
                && value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE //
                && fieldType != Long.class) {
                out.write('L');
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName) {
        final ModelLexer lexer = parser.lexer;

        Long longObject;
        if (lexer.token() == ModelToken.LITERAL_INT) {
            long longValue = lexer.longValue();
            lexer.nextToken(ModelToken.COMMA);
            longObject = Long.valueOf(longValue);
        } else {

            Object value = parser.parse();

            if (value == null) {
                return null;
            }

            longObject = TypeUtils.castToLong(value);
        }
        
        return clazz == AtomicLong.class //
            ? (T) new AtomicLong(longObject.longValue()) //
            : (T) longObject;
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_INT;
    }
}
