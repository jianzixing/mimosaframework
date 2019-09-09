package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.Query;

import java.util.List;

/**
 * 提供加密和解密算法实现
 */
public interface EncryptionDecryptionFilter {
    void encry(ModelObject object);

    void decry(List<ModelObject> objects, Query query);
}
