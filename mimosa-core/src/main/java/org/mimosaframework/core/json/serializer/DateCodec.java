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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelScanner;
import org.mimosaframework.core.json.parser.deserializer.AbstractDateDeserializer;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.util.IOUtils;
import org.mimosaframework.core.json.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class DateCodec extends AbstractDateDeserializer implements ObjectSerializer, ObjectDeserializer {

    public final static DateCodec instance = new DateCodec();
    
    public void write(ModelSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }
        
        Date date;
        if (object instanceof Date) {
            date = (Date) object;
        } else {
            date = TypeUtils.castToDate(object);
        }
        
        if (out.isEnabled(SerializerFeature.WriteDateUseDateFormat)) {
            DateFormat format = serializer.getDateFormat();
            if (format == null) {
                format = new SimpleDateFormat(Model.DEFFAULT_DATE_FORMAT, serializer.locale);
                format.setTimeZone(serializer.timeZone);
            }
            String text = format.format(date);
            out.writeString(text);
            return;
        }
        
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            if (object.getClass() != fieldType) {
                if (object.getClass() == Date.class) {
                    out.write("new Date(");
                    out.writeLong(((Date) object).getTime());
                    out.write(')');
                } else {
                    out.write('{');
                    out.writeFieldName(Model.DEFAULT_TYPE_KEY);
                    serializer.write(object.getClass().getName());
                    out.writeFieldValue(',', "val", ((Date) object).getTime());
                    out.write('}');
                }
                return;
            }
        }

        long time = date.getTime();
        if (out.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
            char quote = out.isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '\"'; 
            out.write(quote);

            Calendar calendar = Calendar.getInstance(serializer.timeZone, serializer.locale);
            calendar.setTimeInMillis(time);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int millis = calendar.get(Calendar.MILLISECOND);

            char[] buf;
            if (millis != 0) {
                buf = "0000-00-00T00:00:00.000".toCharArray();
                IOUtils.getChars(millis, 23, buf);
                IOUtils.getChars(second, 19, buf);
                IOUtils.getChars(minute, 16, buf);
                IOUtils.getChars(hour, 13, buf);
                IOUtils.getChars(day, 10, buf);
                IOUtils.getChars(month, 7, buf);
                IOUtils.getChars(year, 4, buf);

            } else {
                if (second == 0 && minute == 0 && hour == 0) {
                    buf = "0000-00-00".toCharArray();
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                } else {
                    buf = "0000-00-00T00:00:00".toCharArray();
                    IOUtils.getChars(second, 19, buf);
                    IOUtils.getChars(minute, 16, buf);
                    IOUtils.getChars(hour, 13, buf);
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                }
            }
            
            out.write(buf);
            
            int timeZone = calendar.getTimeZone().getRawOffset()/(3600*1000);
            if (timeZone == 0) {
                out.write('Z');
            } else {
                if (timeZone > 0) {
                    out.append('+').append(String.format("%02d", timeZone));
                } else {
                    out.append('-').append(String.format("%02d", -timeZone));
                }
                out.append(":00");
            }

            out.write(quote);
        } else {
            out.writeLong(time);
        }
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultModelParser parser, Type clazz, Object fieldName, Object val) {

        if (val == null) {
            return null;
        }

        if (val instanceof Date) {
            return (T) val;
        } else if (val instanceof Number) {
            return (T) new Date(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            ModelScanner dateLexer = new ModelScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch(false)) {
                    Calendar calendar = dateLexer.getCalendar();
                    
                    if (clazz == Calendar.class) {
                        return (T) calendar;
                    }
                    
                    return (T) calendar.getTime();
                }
            } finally {
                dateLexer.close();
            }
            
            if (strVal.length() == parser.getDateFomartPattern().length()) {
                DateFormat dateFormat = parser.getDateFormat();
                try {
                    return (T) dateFormat.parse(strVal);
                } catch (ParseException e) {
                    // skip
                }
            }
            
            if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
                String dotnetDateStr = strVal.substring(6, strVal.length() - 2);
                strVal = dotnetDateStr;
            }
            
//            JSONScanner iso8601Lexer = new JSONScanner(strVal);
//            if (iso8601Lexer.scanISO8601DateIfMatch()) {
//                val = iso8601Lexer.getCalendar().getTime();
//            }
//            iso8601Lexer.close();
//            
            long longVal = Long.parseLong(strVal);
            return (T) new Date(longVal);
        }

        throw new ModelException("parse error");
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_INT;
    }

}
