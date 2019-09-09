package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelScanner;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.ModelToken;

public class SqlDateDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {

    public final static SqlDateDeserializer instance = new SqlDateDeserializer();
    public final static SqlDateDeserializer instance_timestamp = new SqlDateDeserializer(true);
    
    private boolean                           timestamp = false;
    
    public SqlDateDeserializer() {
        
    }
    
    public SqlDateDeserializer(boolean timestmap) {
        this.timestamp = true;
    }

    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultModelParser parser, Type clazz, Object fieldName, Object val) {
        if (timestamp) {
            return castTimestamp(parser, clazz, fieldName, val);
        }
        
        if (val == null) {
            return null;
        }

        if (val instanceof Date) {
            val = new java.sql.Date(((Date) val).getTime());
        } else if (val instanceof Number) {
            val = (T) new java.sql.Date(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            long longVal;

            ModelScanner dateLexer = new ModelScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch()) {
                    longVal = dateLexer.getCalendar().getTimeInMillis();
                } else {

                    DateFormat dateFormat = parser.getDateFormat();
                    try {
                        Date date = (Date) dateFormat.parse(strVal);
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        return (T) sqlDate;
                    } catch (ParseException e) {
                        // skip
                    }

                    longVal = Long.parseLong(strVal);
                }
            } finally {
                dateLexer.close();
            }
            return (T) new java.sql.Date(longVal);
        } else {
            throw new ModelException("parse error : " + val);
        }

        return (T) val;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T castTimestamp(DefaultModelParser parser, Type clazz, Object fieldName, Object val) {

        if (val == null) {
            return null;
        }

        if (val instanceof Date) {
            return (T) new java.sql.Timestamp(((Date) val).getTime());
        }

        if (val instanceof Number) {
            return (T) new java.sql.Timestamp(((Number) val).longValue());
        }

        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            long longVal;
            ModelScanner dateLexer = new ModelScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch()) {
                    longVal = dateLexer.getCalendar().getTimeInMillis();
                } else {

                    DateFormat dateFormat = parser.getDateFormat();
                    try {
                        Date date = (Date) dateFormat.parse(strVal);
                        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
                        return (T) sqlDate;
                    } catch (ParseException e) {
                        // skip
                    }

                    longVal = Long.parseLong(strVal);
                }
            } finally {
                dateLexer.close();
            }

            return (T) new java.sql.Timestamp(longVal);
        }

        throw new ModelException("parse error");
    }

    public int getFastMatchToken() {
        return ModelToken.LITERAL_INT;
    }
}
