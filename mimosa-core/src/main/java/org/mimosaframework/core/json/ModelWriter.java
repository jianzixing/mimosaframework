package org.mimosaframework.core.json;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

import org.mimosaframework.core.json.serializer.ModelSerializer;
import org.mimosaframework.core.json.serializer.SerializeWriter;
import org.mimosaframework.core.json.serializer.SerializerFeature;

public class ModelWriter implements Closeable, Flushable {

    private SerializeWriter writer;

    private ModelSerializer serializer;

    private ModelStreamContext context;

    public ModelWriter(Writer out){
        writer = new SerializeWriter(out);
        serializer = new ModelSerializer(writer);
    }

    public void config(SerializerFeature feature, boolean state) {
        this.writer.config(feature, state);
    }

    public void startObject() {
        if (context != null) {
            beginStructure();
        }
        context = new ModelStreamContext(context, ModelStreamContext.StartObject);
        writer.write('{');
    }

    public void endObject() {
        writer.write('}');
        endStructure();
    }

    public void writeKey(String key) {
        writeObject(key);
    }

    public void writeValue(Object object) {
        writeObject(object);
    }

    public void writeObject(String object) {
        beforeWrite();

        serializer.write(object);

        afterWriter();
    }

    public void writeObject(Object object) {
        beforeWrite();
        serializer.write(object);
        afterWriter();
    }

    public void startArray() {
        if (context != null) {
            beginStructure();
        }

        context = new ModelStreamContext(context, ModelStreamContext.StartArray);
        writer.write('[');
    }

    private void beginStructure() {
        final int state = context.state;
        switch (context.state) {
            case ModelStreamContext.PropertyKey:
                writer.write(':');
                break;
            case ModelStreamContext.ArrayValue:
                writer.write(',');
                break;
            case ModelStreamContext.StartObject:
                break;
            case ModelStreamContext.StartArray:
                break;
            default:
                throw new ModelException("illegal state : " + state);
        }
    }

    public void endArray() {
        writer.write(']');
        endStructure();
    }

    private void endStructure() {
        context = context.parent;

        if (context == null) {
            return;
        }
        
        int newState = -1;
        switch (context.state) {
            case ModelStreamContext.PropertyKey:
                newState = ModelStreamContext.PropertyValue;
                break;
            case ModelStreamContext.StartArray:
                newState = ModelStreamContext.ArrayValue;
                break;
            case ModelStreamContext.ArrayValue:
                break;
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

    private void beforeWrite() {
        if (context == null) {
            return;
        }
        
        switch (context.state) {
            case ModelStreamContext.StartObject:
            case ModelStreamContext.StartArray:
                break;
            case ModelStreamContext.PropertyKey:
                writer.write(':');
                break;
            case ModelStreamContext.PropertyValue:
                writer.write(',');
                break;
            case ModelStreamContext.ArrayValue:
                writer.write(',');
                break;
            default:
                break;
        }
    }

    private void afterWriter() {
        if (context == null) {
            return;
        }

        final int state = context.state;
        int newState = -1;
        switch (state) {
            case ModelStreamContext.PropertyKey:
                newState = ModelStreamContext.PropertyValue;
                break;
            case ModelStreamContext.StartObject:
            case ModelStreamContext.PropertyValue:
                newState = ModelStreamContext.PropertyKey;
                break;
            case ModelStreamContext.StartArray:
                newState = ModelStreamContext.ArrayValue;
                break;
            case ModelStreamContext.ArrayValue:
                break;
            default:
                break;
        }

        if (newState != -1) {
            context.state = newState;
        }
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }

    @Deprecated
    public void writeStartObject() {
        startObject();
    }

    @Deprecated
    public void writeEndObject() {
        endObject();
    }

    @Deprecated
    public void writeStartArray() {
        startArray();
    }

    @Deprecated
    public void writeEndArray() {
        endArray();
    }
}
