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

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.parser.ModelToken;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class StringCodec implements ObjectSerializer, ObjectDeserializer {

    public static StringCodec instance = new StringCodec();

    public void write(ModelSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
                                                                                                               throws IOException {
        write(serializer, (String) object);
    }

    public void write(ModelSerializer serializer, String value) {
        SerializeWriter out = serializer.out;

        if (value == null) {
            out.writeNull(SerializerFeature.WriteNullStringAsEmpty);
            return;
        }

        out.writeString(value);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultModelParser parser, Type clazz, Object fieldName) {
        if (clazz == StringBuffer.class) {
            final ModelLexer lexer = parser.lexer;
            if (lexer.token() == ModelToken.LITERAL_STRING) {
                String val = lexer.stringVal();
                lexer.nextToken(ModelToken.COMMA);

                return (T) new StringBuffer(val);
            }

            Object value = parser.parse();

            if (value == null) {
                return null;
            }

            return (T) new StringBuffer(value.toString());
        }

        if (clazz == StringBuilder.class) {
            final ModelLexer lexer = parser.lexer;
            if (lexer.token() == ModelToken.LITERAL_STRING) {
                String val = lexer.stringVal();
                lexer.nextToken(ModelToken.COMMA);

                return (T) new StringBuilder(val);
            }

            Object value = parser.parse();

            if (value == null) {
                return null;
            }

            return (T) new StringBuilder(value.toString());
        }

        return (T) deserialze(parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultModelParser parser) {
        final ModelLexer lexer = parser.getLexer();
        if (lexer.token() == ModelToken.LITERAL_STRING) {
            String val = lexer.stringVal();
            lexer.nextToken(ModelToken.COMMA);
            return (T) val;
        }

        if (lexer.token() == ModelToken.LITERAL_INT) {
            String val = lexer.numberString();
            lexer.nextToken(ModelToken.COMMA);
            return (T) val;
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        return (T) value.toString();
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_STRING;
    }
}
