package org.mimosaframework.core.utils.i18n;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class Messages {
    private static final Map<String, Map<String, String>> i18n = new HashMap<>();

    public static synchronized void register(String lanType, List<MessageWords> messageWords) {
        if (lanType != null && messageWords != null) {
            for (MessageWords words : messageWords) {
                Map<String, String> map = i18n.get(lanType);
                if (map == null) map = new HashMap<>();
                map.putAll(words.getWords());
                i18n.put(lanType, map);
            }
        }
    }

    public static synchronized void register(MessagesRegister register) {
        if (register != null) {
            String[] lans = register.getLanTypes();
            for (String lan : lans) {
                register(lan, register.getMessages());
            }
        }
    }

    public static String get(String project, Class c, String key, String... replaces) {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();

        Map<String, String> map = i18n.get(language);
        if (map != null) {
            String value = map.get(project + "_" + c.getName() + "_" + key);
            if (value != null) {
                if (replaces != null) {
                    value = String.format(value, replaces);
                }
                return value;
            }
        }

        map = i18n.get("default");
        if (map != null) {
            String value = map.get(key);
            if (value != null) {
                if (replaces != null) {
                    value = String.format(value, replaces);
                }
                return value;
            }
        }

        return "not found i18n by " + key;
    }
}
