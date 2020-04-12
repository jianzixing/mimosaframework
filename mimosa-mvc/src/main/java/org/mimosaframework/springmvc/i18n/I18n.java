package org.mimosaframework.springmvc.i18n;

import org.mimosaframework.core.utils.i18n.Messages;

public class I18n {
    private static boolean isRegister = false;

    public static void register() {
        if (!isRegister) {
            Messages.register(new LanguageMessageDefault());
            isRegister = true;
        }
    }

    public static String print(String key, String... params) {
        return Messages.get("mimosa_mvc", key, params);
    }
}
