package org.mimosaframework.core.utils.i18n;

import java.util.List;

public interface MessagesRegister {
    String[] getLanTypes();

    List<MessageWords> getMessages();
}
