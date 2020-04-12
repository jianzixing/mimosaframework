package org.mimosaframework.springmvc.i18n;

import org.mimosaframework.core.utils.i18n.MessageWords;
import org.mimosaframework.core.utils.i18n.MessagesRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageMessageDefault implements MessagesRegister {
    private static List<MessageWords> messageWords = new ArrayList<>();

    static {
        Map<String, String> words = new HashMap<>();
        words.put("success", "处理成功");
        words.put("access_fail", "访问失败");
        words.put("trans_fail", "执行事务失败");
        words.put("duplicate_unique_field", "唯一字段重复");
        messageWords.add(new MessageWords("mimosa_mvc", words));
    }

    @Override
    public String[] getLanTypes() {
        return new String[]{"default", "zh"};
    }

    @Override
    public List<MessageWords> getMessages() {
        return messageWords;
    }
}
