package com.eric.fileshare.service;

import com.eric.fileshare.beans.User;
import com.eric.fileshare.dto.LoginDTO;
import com.eric.fileshare.dto.SignupDTO;
import com.eric.fileshare.exceptions.login.LoginException;
import com.eric.fileshare.exceptions.signup.*;

public interface IUserService {

    User doLogin(LoginDTO loginDTO) throws LoginException;

    void doSignup(SignupDTO signupDTO) throws SignupException;

    long getBalance(String email);
}
