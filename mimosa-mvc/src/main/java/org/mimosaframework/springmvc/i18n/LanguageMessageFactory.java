package org.mimosaframework.springmvc.i18n;

import org.mimosaframework.core.utils.Messages;

public class LanguageMessageFactory {
    private static boolean isRegister = false;

    public static void register() {
        if (!isRegister) {
            Messages.register(new LanguageMessageDefault());
            isRegister = true;
        }
    }
}
