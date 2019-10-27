package org.mimosaframework.springmvc.i18n;

import org.mimosaframework.core.utils.i18n.MessageWords;
import org.mimosaframework.core.utils.i18n.MessagesRegister;
import org.mimosaframework.springmvc.utils.ResponseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageMessageDefault implements MessagesRegister {
    private static List<MessageWords> messageWords = new ArrayList<>();

    static {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("success", "处理成功");
        responseMessage.put("access_fail", "访问失败");
        responseMessage.put("trans_fail", "执行事务失败");
        responseMessage.put("duplicate_unique_field", "唯一字段重复");
        messageWords.add(new MessageWords("mimosa_mvc", ResponseMessage.class, responseMessage));
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
