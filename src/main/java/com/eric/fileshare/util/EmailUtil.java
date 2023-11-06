package com.eric.fileshare.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.Calendar;
import java.util.Random;

public class EmailUtil {

    // 邮箱服务器的地址
    private static final String EMAIL_HOST = "smtp.163.com";
    private static final Integer CODE_LENGTH = 6;
    private static final String NUMS_OPTIONS = "0123456789";

    // 邮箱验证码的过期时间
    public static final int TIME = 30;

    // 邮箱服务器的用户名和授权码
    private static final String GRANT_CODE = System.getenv("GRANT_CODE");
    private static final String USERNAME = System.getenv("EMAIL_ADDRESS");

    private static final Random random = new Random();

    public static void sendEmail(String code, String to) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(EMAIL_HOST);
        email.setSmtpPort(994);
        email.setAuthenticator(new DefaultAuthenticator(USERNAME, GRANT_CODE));
        email.setSSLOnConnect(true);
        email.setFrom(USERNAME);
        email.setSubject("注册验证码");
        email.setMsg("您的注册验证码: <h3>" + code + "</h3>有效时间为 " + TIME + " 分钟");
        email.addTo(to);
        email.send();
    }

    public static void sendLink(String link, String to) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(EMAIL_HOST);
        email.setSmtpPort(994);
        email.setAuthenticator(new DefaultAuthenticator(USERNAME, GRANT_CODE));
        email.setSSLOnConnect(true);
        email.setFrom(USERNAME);
        email.setSubject("文件下载链接");
        email.setMsg("<a>" + link + "</a>");
        email.addTo(to);
        email.send();
    }

    public static String randomCode() {
        StringBuffer code = new StringBuffer();
        random.setSeed(System.nanoTime());
        for(int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(NUMS_OPTIONS.length());
            code.append(NUMS_OPTIONS.charAt(index));
        }
        return code.toString();
    }
}
