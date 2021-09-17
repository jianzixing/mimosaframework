package org.mimosaframework.core.utils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用来查找对象中的值
 */
public class Finder {
    private List<Math> maths = new ArrayList<>();
    private int index = 0;

    public static <T> List<T> get(Object obj, String express) {
        Finder finder = new Finder();
        return finder.search(obj, express);
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
                this.finder(any, obj);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
            return any;
        }
        return null;
    }

    private void finder(List any, Object obj) throws NoSuchFieldException, IllegalAccessException {
        Math math = this.maths.get(index);
        index++;

        if (math == null && obj != null) {
            any.add(obj);
        }
        if (obj == null) {
            return;
        }

        if (obj instanceof List || obj instanceof Set) {
            if (math.type == 1) {
                String str = math.str;
                if (str.equals("*")) {
                    Iterator iterator = ((Collection) obj).iterator();
                    while (iterator.hasNext()) {
                        Object next = iterator.next();
                        this.finder(any, next);
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
                    this.finder(any, next);
                }
            } else {
                throw new IllegalArgumentException("current object is array require object");
            }
        } else if (obj instanceof Map) {
            if (math.type == 0) {
                String str = math.str;
                Object next = ((Map) obj).get(str);
                this.finder(any, next);
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
            this.finder(any, next);
        }
    }

    public static void main(String[] args) {
      
    }

    private class Math {
        String str;
        Integer type;
    }
}
