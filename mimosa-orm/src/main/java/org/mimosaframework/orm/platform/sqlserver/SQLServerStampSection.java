package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampSection;

public class SQLServerStampSection extends PlatformStampSection {
    public SQLServerStampSection() {
        this.setDeclareInBegin(true);
    }

    protected void appendBuilderDeclare(StringBuilder nsb, boolean isIn) {
        for (String s : declares) {
            nsb.append(NL_TAB + "DECLARE " + s + ";");
        }
    }

    protected void appendBuilderWrapper(ExecuteImmediate item, StringBuilder nsb) {
        if (StringTools.isNotEmpty(item.preview)) {
            nsb.append(NL_TAB + item.preview + " ");
        } else {
            nsb.append(NL_TAB);
        }

        if (StringTools.isNotEmpty(item.end)) {
            nsb.append(item.sql + ";");
            nsb.append(item.end + ";");
        } else {
            nsb.append(item.sql + "; ");
        }
    }
}
