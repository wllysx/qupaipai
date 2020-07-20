package com.qupp.client.utils.secretUtils;



import android.util.Log;

import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * @ClassName RsaCipherUtil
 * @Author pdz
 * @Description 非对称加密解密工具类
 * @Date 2019/6/6
 */
public class RsaCipherUtil {
    private RsaCipherUtil() {

    }

    public static final String  PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoLd/wUEkw/Zn496gOYjFPPMgzoZkcRvV6mpVjolwium2GrgxAK5r8AqcEbvXVslrj6LhpVOHYJOgP3oPWigiXiDE0cXh2psmMTk82xphFJa38rAI2U+6W4QfUU04ETGZaWf53AGl6NUFuhmXFqDsSvf+93rvFmHYKjg6dXic0lQIDAQAB";



    /**
     * 用公钥对字符串进行加密
     * @param data 原文
     */
    public static String encrypt(String data) throws Exception {
        // 得到公钥
        PublicKey publicKey = RSAUtils.loadPublicKey(PUBLIC_KEY);
        // 加密数据
        Cipher cp = Cipher.getInstance("RSA/None/PKCS1Padding");
        cp.init(Cipher.ENCRYPT_MODE, publicKey);
        Log.d("secretsstr",cp.doFinal(data.getBytes()).toString());
        String afterencrypt = Base64Utils.encode(cp.doFinal(data.getBytes()));
       // return URLEncoder.encode(afterencrypt, "UTF-8");
        return afterencrypt;
    }

}
