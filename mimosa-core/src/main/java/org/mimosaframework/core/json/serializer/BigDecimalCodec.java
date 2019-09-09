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

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class BigDecimalCodec implements ObjectSerializer, ObjectDeserializer {

    public final static BigDecimalCodec instance = new BigDecimalCodec();

    public void write(ModelSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullNumberAsZero);
        } else {
            BigDecimal val = (BigDecimal) object;
            out.write(val.toString());

            if (out.isEnabled(SerializerFeature.WriteClassName) && fieldType != BigDecimal.class && val.scale() == 0) {
                out.write('.');
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultModelParser parser) {
        final ModelLexer lexer = parser.lexer;
        if (lexer.token() == ModelToken.LITERAL_INT) {
            BigDecimal decimalValue = lexer.decimalValue();
            lexer.nextToken(ModelToken.COMMA);
            return (T) decimalValue;
        }

        if (lexer.token() == ModelToken.LITERAL_FLOAT) {
            BigDecimal val = lexer.decimalValue();
            lexer.nextToken(ModelToken.COMMA);
            return (T) val;
        }

        Object value = parser.parse();
        return value == null //
            ? null //
            : (T) TypeUtils.castToBigDecimal(value);
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_INT;
    }
}
