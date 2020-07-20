package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampSection;

public class PostgreSQLStampSection extends PlatformStampSection {
    protected void appendBuilderBegin(StringBuilder nsb) {
        nsb.append(NL + "DO $BODY$");
    }

    protected void appendBuilderEnd(StringBuilder nsb) {
        nsb.append(NL + "END $BODY$;");
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
