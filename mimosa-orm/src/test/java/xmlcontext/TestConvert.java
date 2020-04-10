package xmlcontext;

import org.mimosaframework.orm.convert.ConvertType;
import org.mimosaframework.orm.convert.NamingConvert;

public class TestConvert implements NamingConvert {
    private String pm;

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        System.out.println(pm + "===");
        this.pm = pm;
    }

    @Override
    public String convert(String name, ConvertType type) {
        if (type.equals(ConvertType.TABLE_NAME)) {
            if (name.toLowerCase().startsWith("table")) {
                return "tab" + name.substring(5);
            }
        }
        if (type.equals(ConvertType.FIELD_NAME)) {
            return "fd" + name;
        }
        return name;
    }
}
