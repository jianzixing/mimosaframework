package org.mimosaframework.orm.convert;

public class H2UMappingNamedConvert implements NamingConvert {
    private ConvertConfig config;

    @Override
    public void setConfig(ConvertConfig config) {
        this.config = config;
    }

    public String convert(String name, ConvertType type) {
        if (name == null || name.equals("")) {
            return "";
        }
        if (name.matches("[a-z_]+")) {
            return config != null && config.isUppercase() ? name.toUpperCase() : name;
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

        return config != null && config.isUppercase() ? tableName.toUpperCase() : tableName;
    }

    @Override
    public String prefix(String name, String prefix) {
        if (name.startsWith(prefix + "_")) {
            return name;
        }
        return config != null && config.isUppercase() ?
                (prefix + "_" + name).toUpperCase() : prefix + "_" + name;
    }
}
