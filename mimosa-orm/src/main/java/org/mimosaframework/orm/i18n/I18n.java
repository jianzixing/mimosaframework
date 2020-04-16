package org.mimosaframework.orm.i18n;

import org.mimosaframework.core.utils.i18n.Messages;

public class I18n {
    public static final String PROJECT = "mimosa_orm";
    private static boolean isRegister = false;

    public static void register() {
        if (!isRegister) {
            Messages.register(new LanguageMessageDefault());
            isRegister = true;
        }
    }

    static {
        register();
    }

    public static String print(String key, String... params) {
        return Messages.get(I18n.PROJECT, key, params);
    }
}
