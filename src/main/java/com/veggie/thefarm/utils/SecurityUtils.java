package com.veggie.thefarm.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Bohyun on 2021.04.16
 */
public class SecurityUtils {

    private static final String AES_SECRET_KEY = "thefarmbohyuneunjinkipar";

    // AES 암호화
    public static String encryptAES(String value) {
        try {
            SecretKeySpec sKeySpec = new SecretKeySpec(AES_SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    // AES 복호화
    public static String decryptAES(String value) {
        try {
            SecretKeySpec sKeySpec = new SecretKeySpec(AES_SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(value.getBytes()));
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    // BCryptPassword 암호화
    public static String encryptBCryptPassword(String value) {
        return new BCryptPasswordEncoder().encode(value);
    }

    // BCryptPassword 일치 여부
    public static boolean matchesBCryptPassword(String comparePassword, String orgPassword) {
        return new BCryptPasswordEncoder().matches(comparePassword, orgPassword);
    }

}
