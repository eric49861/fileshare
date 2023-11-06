package com.eric.fileshare;

import com.eric.fileshare.exceptions.signup.CodeIncorrectException;
import com.eric.fileshare.exceptions.signup.SignupException;
import org.junit.Test;

public class TestException {

    @Test
    public void testException() {
        try {
            throw new CodeIncorrectException("验证码 不正确");
        }catch (CodeIncorrectException e) {
            System.out.println("class name = " + e.getClass().getName());
        }
    }
}
