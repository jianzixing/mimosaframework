package org.mimosaframework.core.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用来查找对象中的值，可以更加表达式参数获取想要的层级对象列表
 * 比如 a.b[*].c.d[1] 表示获取a对象中的数组b的所有值中的c对象中
 * 的d数组第一个值，然后全部装入一个list并返回
 */
public class Finder {
    private List<Math> maths = new ArrayList<>();
    private Math DEFAULT = new Math("*", 1);

    public static <T> List<T> get(Object obj, String express) {
        Finder finder = new Finder();
        return finder.search(obj, express);
    }

    public static <T> List<T> exclude(List<T> retain, List compare, Serializable... name) {
        try {
            if (retain != null && compare != null && name != null && name.length > 0) {
                List<T> rm = new ArrayList<>();
                for (T t : retain) {
                    boolean b = false;
                    for (Object t2 : compare) {
                        int i = 0;
                        for (Serializable s : name) {
                            Object v1 = getObjectValue(t, s);
                            Object v2 = getObjectValue(t2, s);
                            if (v1.equals(v2)) {
                                i++;
                            }
                        }
                        if (i == name.length) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        rm.add(t);
                    }
                }
                retain.removeAll(rm);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return retain;
    }

    public static boolean hasField(Class c, Serializable key) {
        Field[] fields = c.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.getName().equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setObjectValue(Object object, Serializable key, Object value) {
        try {
            if (object instanceof Map) {
                ((Map) object).put(key, value);
            } else {
                Field field1 = object.getClass().getDeclaredField(key.toString());
                field1.setAccessible(true);
                field1.set(object, value);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void removeObjectValue(Object object, Serializable key) {
        try {
            if (object instanceof Map) {
                ((Map) object).remove(key);
            } else {
                Field field1 = object.getClass().getDeclaredField(key.toString());
                field1.setAccessible(true);
                field1.set(object, null);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Object getObjectValue(Object object, Serializable key) {
        try {
            if (object instanceof Map) {
                return ((Map) object).get(key);
            } else {
                Field field1 = object.getClass().getDeclaredField(key.toString());
                field1.setAccessible(true);
                Object v = field1.get(object);
                return v;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String getStringValue(Object obj, Serializable keyName) {
        return String.valueOf(getObjectValue(obj, keyName));
    }

    public static List getArrayValue(Object object, Serializable keyName) {
        return (List) getObjectValue(object, keyName);
    }

    public <T> List<T> search(Object obj, String express) {
        if (obj != null && StringTools.isNotEmpty(express)) {
            String[] s1 = express.split("\\.");
            for (String s2 : s1) {
                String[] s5 = s2.split("\\[");
                Math mathMain = new Math();
                mathMain.type = 0;
                mathMain.str = s5[0];
                this.maths.add(mathMain);
                boolean isMatch = Pattern.matches("\\[.*?\\]", s2);
                if (isMatch) {
                    Pattern r = Pattern.compile("\\[.*?\\]");
                    Matcher m = r.matcher(s2);
                    while (m.find()) {
                        String s3 = m.group(0);
                        if (StringTools.isNotEmpty(s3)) {
                            s3 = s3.substring(1, s3.length() - 1);

                            Math math = new Math();
                            math.type = 1;
                            math.str = s3.trim();
                            this.maths.add(math);
                        }
                    }
                }
            }

            List<T> any = new ArrayList<>();
            try {
                this.finder(any, obj, 0);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
            return any;
        }
        return null;
    }

    private void finder(List any, Object obj, int deep) throws NoSuchFieldException, IllegalAccessException {
        Math math = null;
        if (this.maths.size() > deep) {
            math = this.maths.get(deep);
        }

        if (math == null && obj != null) {
            any.add(obj);
        }
        if (math == null || obj == null) {
            return;
        }

        if (math.type != 1 && (obj instanceof List || obj instanceof Set)) {
            // 这里可以省略[0]这种写法
            math = DEFAULT;
        } else {
            deep = deep + 1;
        }

        if (obj instanceof List || obj instanceof Set) {
            if (math.type == 1) {
                String str = math.str;
                if (str.equals("*")) {
                    Iterator iterator = ((Collection) obj).iterator();
                    while (iterator.hasNext()) {
                        Object next = iterator.next();
                        this.finder(any, next, deep);
                    }
                } else {
                    Integer i = Integer.parseInt(str);
                    Object next = null;
                    if (obj instanceof List) {
                        next = ((List) obj).get(i);
                    }
                    if (obj instanceof Set) {
                        Iterator iterator = ((Set) obj).iterator();
                        int j = 0;
                        while (iterator.hasNext()) {
                            if (j == i) {
                                next = iterator.next();
                            }
                            j++;
                        }
                    }
                    this.finder(any, next, deep);
                }
            } else {
                throw new IllegalArgumentException("current object is array require object");
            }
        } else if (obj instanceof Map) {
            if (math.type == 0) {
                String str = math.str;
                Object next = ((Map) obj).get(str);
                this.finder(any, next, deep);
            } else {
                throw new IllegalArgumentException("current object is object require array");
            }
        } else {
            if (math.type == 1) {
                throw new IllegalArgumentException("current object is object require array");
            }
            String str = math.str;
            Field field = obj.getClass().getDeclaredField(str);
            field.setAccessible(true);
            Object next = field.get(obj);
            this.finder(any, next, deep);
        }
    }

    public static void main(String[] args) {

    }

    private class Math {
        String str;
        Integer type;

        public Math() {
        }

        public Math(String str, Integer type) {
            this.str = str;
            this.type = type;
        }
    }
}
