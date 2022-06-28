package org.mimosaframework.core.encryption;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMacSHA1Utils {
    public static String encrypt(String paramStr, String key) {
        try {
            byte[] data = key.getBytes("UTF-8");
            byte[] text = paramStr.getBytes("UTF-8");
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] r = mac.doFinal(text);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < r.length; i++) {
                int val = ((int) r[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
