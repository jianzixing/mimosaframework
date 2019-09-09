package org.mimosaframework.core.json;

import java.io.Closeable;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.mimosaframework.core.json.util.TypeUtils;
import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.ModelReaderScanner;
import org.mimosaframework.core.json.parser.ModelToken;

public class ModelReader implements Closeable {

    private final DefaultModelParser parser;
    private ModelStreamContext context;

    public ModelReader(Reader reader){
        this(reader, new Feature[0]);
    }
    
    public ModelReader(Reader reader, Feature... features){
        this(new ModelReaderScanner(reader));
        for (Feature feature : features) {
            this.config(feature, true);
        }
    }

    public ModelReader(ModelLexer lexer){
        this(new DefaultModelParser(lexer));
    }

    public ModelReader(DefaultModelParser parser){
        this.parser = parser;
    }
    
    public void setTimzeZone(TimeZone timezone) {
        this.parser.lexer.setTimeZone(timezone);
    }
    
    public void setLocale(Locale locale) {
        this.parser.lexer.setLocale(locale);
    }

    public void config(Feature feature, boolean state) {
        this.parser.config(feature, state);
    }
    
    public Locale getLocal() {
        return this.parser.lexer.getLocale();
    }
    
    public TimeZone getTimzeZone() {
        return this.parser.lexer.getTimeZone();
    }

    public void startObject() {
        if (context == null) {
            context = new ModelStreamContext(null, ModelStreamContext.StartObject);
        } else {
            startStructure();
            context = new ModelStreamContext(context, ModelStreamContext.StartObject);
        }

        this.parser.accept(ModelToken.LBRACE, ModelToken.IDENTIFIER);
    }

    public void endObject() {
        this.parser.accept(ModelToken.RBRACE);
        endStructure();
    }

    public void startArray() {
        if (context == null) {
            context = new ModelStreamContext(null, ModelStreamContext.StartArray);
        } else {
            startStructure();

            context = new ModelStreamContext(context, ModelStreamContext.StartArray);
        }
        this.parser.accept(ModelToken.LBRACKET);
    }

    public void endArray() {
        this.parser.accept(ModelToken.RBRACKET);
        endStructure();
    }

    private void startStructure() {
        final int state = context.state;
        switch (state) {
            case ModelStreamContext.PropertyKey:
                parser.accept(ModelToken.COLON);
                break;
            case ModelStreamContext.PropertyValue:
            case ModelStreamContext.ArrayValue:
                parser.accept(ModelToken.COMMA);
                break;
            case ModelStreamContext.StartArray:
            case ModelStreamContext.StartObject:
                break;
            default:
                throw new ModelException("illegal state : " + context.state);
        }
    }

    private void endStructure() {
        context = context.parent;

        if (context == null) {
            return;
        }
        
        final int state = context.state;
        int newState = -1;
        switch (state) {
            case ModelStreamContext.PropertyKey:
                newState = ModelStreamContext.PropertyValue;
                break;
            case ModelStreamContext.StartArray:
                newState = ModelStreamContext.ArrayValue;
                break;
            case ModelStreamContext.PropertyValue:
            case ModelStreamContext.StartObject:
                newState = ModelStreamContext.PropertyKey;
                break;
            default:
                break;
        }
        if (newState != -1) {
            context.state = newState;
        }
    }

    public boolean hasNext() {
        if (context == null) {
            throw new ModelException("context is null");
        }

        final int token = parser.lexer.token();
        final int state = context.state;
        switch (state) {
            case ModelStreamContext.StartArray:
            case ModelStreamContext.ArrayValue:
                return token != ModelToken.RBRACKET;
            case ModelStreamContext.StartObject:
            case ModelStreamContext.PropertyValue:
                return token != ModelToken.RBRACE;
            default:
                throw new ModelException("illegal state : " + state);
        }
    }

    public int peek() {
        return parser.lexer.token();
    }

    public void close() {
        parser.close();
    }

    public Integer readInteger() {
        Object object;
        if (context == null) {
            object = parser.parse();
        } else {
            readBefore();
            object = parser.parse();
            readAfter();
        }

        return TypeUtils.castToInt(object);
    }

    public Long readLong() {
        Object object;
        if (context == null) {
            object = parser.parse();
        } else {
            readBefore();
            object = parser.parse();
            readAfter();
        }

        return TypeUtils.castToLong(object);
    }

    public String readString() {
        Object object;
        if (context == null) {
            object = parser.parse();
        } else {
            readBefore();
            ModelLexer lexer = parser.lexer;
            if (context.state == ModelStreamContext.StartObject && lexer.token() == ModelToken.IDENTIFIER) {
                object = lexer.stringVal();
                lexer.nextToken();
            } else {
                object = parser.parse();
            }
            readAfter();
        }

        return TypeUtils.castToString(object);
    }
    
    public <T> T readObject(TypeReference<T> typeRef) {
        return readObject(typeRef.getType());
    }

    public <T> T readObject(Type type) {
        if (context == null) {
            return parser.parseObject(type);
        }

        readBefore();
        T object = parser.parseObject(type);
        readAfter();
        return object;
    }

    public <T> T readObject(Class<T> type) {
        if (context == null) {
            return parser.parseObject(type);
        }

        readBefore();
        T object = parser.parseObject(type);
        readAfter();
        return object;
    }

    public void readObject(Object object) {
        if (context == null) {
            parser.parseObject(object);
            return;
        }

        readBefore();
        parser.parseObject(object);
        readAfter();
    }

    public Object readObject() {
        if (context == null) {
            return parser.parse();
        }

        readBefore();
        Object object;
        switch (context.state) {
            case ModelStreamContext.StartObject:
            case ModelStreamContext.PropertyValue:
                object = parser.parseKey();
                break;
            default:
                object = parser.parse();
                break;
        }

        readAfter();
        return object;
    }

    @SuppressWarnings("rawtypes")
    public Object readObject(Map object) {
        if (context == null) {
            return parser.parseObject(object);
        }

        readBefore();
        Object value = parser.parseObject(object);
        readAfter();
        return value;
    }

    private void readBefore() {
        int state = context.state;
        // before
        switch (state) {
            case ModelStreamContext.PropertyKey:
                parser.accept(ModelToken.COLON);
                break;
            case ModelStreamContext.PropertyValue:
                parser.accept(ModelToken.COMMA, ModelToken.IDENTIFIER);
                break;
            case ModelStreamContext.ArrayValue:
                parser.accept(ModelToken.COMMA);
                break;
            case ModelStreamContext.StartObject:
                break;
            case ModelStreamContext.StartArray:
                break;
            default:
                throw new ModelException("illegal state : " + state);
        }
    }

    private void readAfter() {
        int state = context.state;
        int newStat = -1;
        switch (state) {
            case ModelStreamContext.StartObject:
                newStat = ModelStreamContext.PropertyKey;
                break;
            case ModelStreamContext.PropertyKey:
                newStat = ModelStreamContext.PropertyValue;
                break;
            case ModelStreamContext.PropertyValue:
                newStat = ModelStreamContext.PropertyKey;
                break;
            case ModelStreamContext.ArrayValue:
                break;
            case ModelStreamContext.StartArray:
                newStat = ModelStreamContext.ArrayValue;
                break;
            default:
                throw new ModelException("illegal state : " + state);
        }
        if (newStat != -1) {
            context.state = newStat;
        }
    }

}
