package com.eric.fileshare;

import com.eric.fileshare.util.EmailUtil;
import org.apache.commons.mail.EmailException;
import org.junit.Test;

import java.util.Map;

public class TestEmail {

    @Test
    public void TestRandomCode(){
        for (int i = 0; i < 10; i++) {
            System.out.println(EmailUtil.randomCode());
        }
    }
    @Test
    public void TestSendEmail() {
        String code = EmailUtil.randomCode();
        try {
            EmailUtil.sendEmail(code, "1585416826@qq.com");
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestGetEnv() {
        String emailAddress = System.getenv("EMAIL_ADDRESS");
        String grantCode = System.getenv("GRANT_CODE");

        assert emailAddress != null;
        System.out.println("emailAddress = " + emailAddress);

        assert grantCode != null;
        System.out.println("grant_code = " + grantCode);
    }

    @Test
    public void testGetAllEnv() {
        Map<String, String> getenv = System.getenv();
        for(String key : getenv.keySet()) {
            System.out.println("{key = " + key + ", value = " + getenv.get(key) + "}");
        }
    }
}
