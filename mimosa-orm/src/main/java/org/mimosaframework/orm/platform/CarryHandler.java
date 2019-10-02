package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.ModelObject;

import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class CarryHandler {
    protected ActionDataSourceWrapper dswrapper;

    public CarryHandler(ActionDataSourceWrapper dswrapper) {
        this.dswrapper = dswrapper;
    }

    public abstract Object doHandler(PorterStructure structure) throws SQLException;

    protected void processClobType(ModelObject object) throws SQLException {
        if (object != null) {
            Set<Map.Entry<Object, Object>> set = object.entrySet();
            Map<Object, Object> map = null;
            for (Map.Entry<Object, Object> entry : set) {
                Object value = entry.getValue();
                if (value != null && value instanceof Clob) {
                    if (map == null) {
                        map = new HashMap<>();
                    }
                    Clob clob = (Clob) value;
                    Reader reader = clob.getCharacterStream();

                    StringBuilder buf = new StringBuilder();

                    try {
                        char[] chars = new char[2048];
                        for (; ; ) {
                            int len = reader.read(chars, 0, chars.length);
                            if (len < 0) {
                                break;
                            }
                            buf.append(chars, 0, len);
                        }
                    } catch (Exception ex) {
                        throw new ModelException("read string from reader error", ex);
                    }

                    String text = buf.toString();
                    map.put(entry.getKey(), text);
                }
            }

            if (map != null) {
                Set<Map.Entry<Object, Object>> vs = map.entrySet();
                for (Map.Entry<Object, Object> entry : vs) {
                    object.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
