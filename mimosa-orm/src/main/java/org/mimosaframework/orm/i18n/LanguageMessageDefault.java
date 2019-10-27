package org.mimosaframework.orm.i18n;

import org.mimosaframework.core.utils.MessagesRegister;

import java.util.HashMap;
import java.util.Map;

public class LanguageMessageDefault implements MessagesRegister {
    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("mimosa_mvc.response_message.success", "处理成功");
        map.put("mimosa_mvc.response_message.access_fail", "访问失败");
        map.put("mimosa_mvc.response_message.trans_fail", "执行事务失败");
        map.put("mimosa_mvc.response_message.duplicate_unique_field", "唯一字段重复");
    }

    @Override
    public String[] getLanTypes() {
        return new String[]{"default", "zh"};
    }

    @Override
    public Map<String, String> getMessages() {
        return map;
    }
}
