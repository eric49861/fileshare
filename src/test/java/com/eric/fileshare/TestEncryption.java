package com.eric.fileshare;

import com.eric.fileshare.util.EncryptionUtil;
import org.junit.Test;

import java.util.UUID;

public class TestEncryption {

    @Test
    public void TestMD5() {
        String s = "helloworld";
        String s1 = EncryptionUtil.md5(s);
        System.out.println("s1 = " + s1);
    }

    @Test
    public void testUUID() {
        for (int i = 0; i < 10; i++) {
            String s = UUID.randomUUID().toString();
            System.out.println(s);
        }
    }
}
