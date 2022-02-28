package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.serializer.ModelSerializer;
import org.mimosaframework.core.json.serializer.SerializeWriter;
import org.mimosaframework.core.json.serializer.ToStringSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class DyToStringSerializer extends ToStringSerializer {
    public static DyToStringSerializer instance = new DyToStringSerializer();

    @Override
    public void write(ModelSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        // 为了兼容js端的精度丢失问题编写的转换类
        if (object instanceof Long || object.getClass().isAssignableFrom(int.class)) {
            long l = (long) object;
            if (l > Integer.MAX_VALUE) {
                String strVal = object.toString();
                out.writeString(strVal);
            } else {
                out.writeLong(l);
            }
        } else {
            String strVal = object.toString();
            out.writeString(strVal);
        }
    }
}
