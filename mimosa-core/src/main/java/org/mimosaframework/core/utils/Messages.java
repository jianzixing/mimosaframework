package org.mimosaframework.core.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class Messages {
    private static final Map<String, Map<String, String>> i18n = new HashMap<>();

    public static synchronized void register(String lanType, Map<String, String> words) {
        if (lanType != null && words != null) {
            i18n.put(lanType, words);
        }
    }

    public static synchronized void register(MessagesRegister register) {
        if (register != null) {
            String[] lans = register.getLanTypes();
            for (String lan : lans) {
                i18n.put(lan, register.getMessages());
            }
        }
    }

    public static String get(String key) {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();

        Map<String, String> map = i18n.get(language);
        if (map != null) {
            String value = map.get(key);
            if (value != null) {
                return value;
            }
        }

        map = i18n.get("default");
        if (map != null) {
            String value = map.get(key);
            if (value != null) {
                return value;
            }
        }

        return "not found i18n by " + key;
    }
}
