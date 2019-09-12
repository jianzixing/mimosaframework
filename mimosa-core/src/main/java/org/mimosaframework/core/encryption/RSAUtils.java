package org.mimosaframework.core.encryption;


import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";

    public static class RSA {
        private int keySize = 1024;
        private String publicKeyString;
        private String privateKeyString;

        public RSA(int keySize) {
            this.keySize = keySize;
            this.init();
        }

        public RSA() {
            init();
        }

        public RSA(String publicKeyString, String privateKeyString) {
            this.publicKeyString = publicKeyString;
            this.privateKeyString = privateKeyString;
        }

        public String getPublicKeyString() {
            return publicKeyString;
        }

        public String getPrivateKeyString() {
            return privateKeyString;
        }

        private void init() {
            //为RSA算法创建一个KeyPairGenerator对象
            KeyPairGenerator kpg;
            try {
                kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
            }

            //初始化KeyPairGenerator对象,密钥长度
            kpg.initialize(keySize);
            //生成密匙对
            KeyPair keyPair = kpg.generateKeyPair();
            //得到公钥
            Key publicKey = keyPair.getPublic();
            publicKeyString = Base64.encode(publicKey.getEncoded());
            //得到私钥
            Key privateKey = keyPair.getPrivate();
            privateKeyString = Base64.encode(privateKey.getEncoded());
        }

        /**
         * 得到公钥
         */
        public RSAPublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
            //通过X509编码的Key指令获得公钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
            return key;
        }

        /**
         * 得到私钥
         */
        public RSAPrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
            //通过PKCS#8编码的Key指令获得私钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
            RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            return key;
        }

        /**
         * 公钥加密
         *
         * @param data
         * @return 公钥字符串
         */
        public String publicEncrypt(String data) {
            try {
                return publicEncrypt(data, this.getPublicKey());
            } catch (Exception e) {
                throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
            }
        }

        private String publicEncrypt(String data, RSAPublicKey publicKey) {
            try {
                Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return Base64.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
            } catch (Exception e) {
                throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
            }
        }

        /**
         * 私钥解密
         *
         * @param data 加密数据
         * @return 私钥字符串
         */

        public String privateDecrypt(String data) {
            try {
                return privateDecrypt(data, this.getPrivateKey());
            } catch (Exception e) {
                throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
            }
        }

        public String privateDecrypt(String data, RSAPrivateKey privateKey) {
            try {
                Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
            } catch (Exception e) {
                throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
            }
        }

        /**
         * 私钥加密
         */

        public String privateEncrypt(String data) {
            try {
                return privateEncrypt(data, this.getPrivateKey());
            } catch (Exception e) {
                throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
            }
        }

        public String privateEncrypt(String data, RSAPrivateKey privateKey) {
            try {
                Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, privateKey);
                return Base64.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
            } catch (Exception e) {
                throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
            }
        }

        /**
         * 公钥解密
         */
        public String publicDecrypt(String data) {
            try {
                return publicDecrypt(data, this.getPublicKey());
            } catch (Exception e) {
                throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
            }
        }

        public String publicDecrypt(String data, RSAPublicKey publicKey) {
            try {
                Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, publicKey);
                return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
            } catch (Exception e) {
                throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
            }
        }

        private byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) throws IOException {
            int maxBlock = 0;
            if (opmode == Cipher.DECRYPT_MODE) {
                maxBlock = keySize / 8;
            } else {
                maxBlock = keySize / 8 - 11;
            }
            ByteArrayOutputStream out = null;
            try {

                out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] buff;
                int i = 0;
                try {
                    while (datas.length > offSet) {
                        if (datas.length - offSet > maxBlock) {
                            buff = cipher.doFinal(datas, offSet, maxBlock);
                        } else {
                            buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                        }
                        out.write(buff, 0, buff.length);
                        i++;
                        offSet = i * maxBlock;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
                }
                byte[] resultDatas = out.toByteArray();
                return resultDatas;
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    public static RSA builder() {
        return new RSA();
    }

    public static RSA builder(int size) {
        return new RSA(size);
    }

    public static RSA builder(String publicKey, String privateKey) {
        return new RSA(publicKey, privateKey);
    }

    public static void main(String[] args) throws Exception {
        RSA rsa = RSAUtils.builder(1024);
        String gy = rsa.getPublicKeyString();
        System.out.println("公钥: \n\r" + gy);
        System.out.println("私钥： \n\r" + rsa.getPrivateKeyString());

        System.out.println("公钥加密——私钥解密");
        String str = "站在大明门前守卫的禁卫军，事先没有接到\n" +
                "有关的命令，但看到大批盛装的官员来临，也就\n" +
                "以为确系举行大典，因而未加询问。进大明门即\n" +
                "为皇城。文武百官看到端门午门之前气氛平静，\n" +
                "城楼上下也无朝会的迹象，既无几案，站队点名\n" +
                "的御史和御前侍卫“大汉将军”也不见踪影，不免\n" +
                "心中揣测，互相询问：所谓午朝是否讹传？";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        String encodedData = rsa.publicEncrypt(str);
        System.out.println("密文：\r\n" + encodedData);
        String decodedData = rsa.privateDecrypt(encodedData);
        System.out.println("解密后文字: \r\n" + decodedData);

        if (!str.equals(decodedData)) {
            throw new Exception("不一样");
        }

        System.out.println(rsa.publicDecrypt(rsa.privateEncrypt(str)));
        System.out.println();
        System.out.println();
        System.out.println();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            RSA rsa2 = RSAUtils.builder(1024);
            String a = rsa2.getPrivateKeyString();
            String b = rsa2.getPublicKeyString();
            byte[] bytes1 = a.getBytes("UTF-8");
            int count = 0;
            for (byte bb : bytes1) {
                sb1.append(bb);
                if (count != bytes1.length - 1) {
                    sb1.append(",");
                }
                count++;
            }
            sb1.append("\r\n");

            byte[] bytes2 = b.getBytes("UTF-8");
            count = 0;
            for (byte bb : bytes2) {
                sb2.append(bb);
                if (count != bytes2.length - 1) {
                    sb2.append(",");
                }
                count++;
            }
            sb2.append("\r\n");
        }

        System.out.println(sb1.toString());
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(sb2.toString());
    }
}
