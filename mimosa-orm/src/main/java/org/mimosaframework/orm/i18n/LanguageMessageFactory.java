package org.mimosaframework.orm.i18n;

import org.mimosaframework.core.utils.i18n.Messages;

public class LanguageMessageFactory {
    public static final String PROJECT = "mimosa_orm";
    private static boolean isRegister = false;

    public static void register() {
        if (!isRegister) {
            Messages.register(new LanguageMessageDefault());
            isRegister = true;
        }
    }
}
