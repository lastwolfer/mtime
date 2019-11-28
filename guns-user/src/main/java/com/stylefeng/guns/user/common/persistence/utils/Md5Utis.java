package com.stylefeng.guns.user.common.persistence.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utis {
    public static String getMd5(String content){
        byte[] bytes = content.getBytes();
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest = md5.digest(bytes);
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                int x = b & 0xff;
                String s = Integer.toHexString(x);
                if(s.length() == 1){
                    sb.append(0);
                }
                sb.append(s);
                return sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
