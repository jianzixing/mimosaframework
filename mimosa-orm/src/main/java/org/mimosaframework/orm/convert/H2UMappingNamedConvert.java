package org.mimosaframework.orm.convert;

public class H2UMappingNamedConvert implements MappingNamedConvert {

    private static char[] a_z = "abcdefghijklmnopqrstwvuxyz".toCharArray();
    private static char[] A_Z = "abcdefghijklmnopqrstwvuxyz".toUpperCase().toCharArray();

    public String convert(String name) {
        if (name == null || name.equals("")) {
            return "";
        }
        if (name.matches("[a-z_]+")) {
            return name;
        }
        StringBuilder column = new StringBuilder();
        column.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            // 在小写字母前添加下划线
            if (!Character.isDigit(s.charAt(0)) && s.equals(s.toUpperCase())) {
                column.append("_");
            }
            // 其他字符直接转成小写
            column.append(s.toLowerCase());
        }

        String tableName = column.toString();

        if (tableName.startsWith("table_")) {
            tableName = tableName.replaceFirst("table_", "t_");
        }

        return tableName;
    }

    public String reverse(String name) {
        if (name == null || name.equals("")) {
            return "";
        }

        if (name.startsWith("t_")) {
            name = name.replaceFirst("t_", "table_");
        }

        StringBuilder sb = new StringBuilder(name.length());
        // 当前的下标
        int i = 0;
        int length = name.length();
        for (int j = 0; j < length; j++) {
            if (name.charAt(j) == '_') {
                // 判断后面是否还有_
                while (name.charAt(++j) == '_') {
                }
                i = j;// i所对应的字符需要转换为大写字符
                char c = name.charAt(i);
                if (sb.length() == 0) {
                } else {
                    for (int k = 0; k < a_z.length; k++) {

                        if (a_z[k] == c) {
                            c = A_Z[k];
                            break;
                        }
                    }
                }
                sb.append(c);
            } else {
                sb.append(name.charAt(j));
            }
        }

        return sb.toString();
    }

}
