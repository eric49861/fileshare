package com.eric.fileshare.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class EncryptionUtil {

    public static String md5(String s) {
        return DigestUtils.md5DigestAsHex(s.getBytes(StandardCharsets.UTF_8));
    }

    public static String md5(byte[] s) {
        return DigestUtils.md5DigestAsHex(s);
    }
}
