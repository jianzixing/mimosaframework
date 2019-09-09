package org.mimosaframework.core.json;

import org.mimosaframework.core.json.serializer.ModelSerializable;
import org.mimosaframework.core.json.serializer.ModelSerializer;
import org.mimosaframework.core.json.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ModelPObject implements ModelSerializable {

    private String function;

    private final List<Object> parameters = new ArrayList<Object>();

    public ModelPObject() {

    }

    public ModelPObject(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void addParameter(Object parameter) {
        this.parameters.add(parameter);
    }

    public String toJSONString() {
        return toString();
    }

    public void write(ModelSerializer serializer, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter writer = serializer.out;
        writer.write(function);
        writer.write('(');
        for (int i = 0; i < parameters.size(); ++i) {
            if (i != 0) {
                writer.write(',');
            }
            serializer.write(parameters.get(i));
        }
        writer.write(')');
    }

    public String toString() {
        return Model.toJSONString(this);
    }
}
