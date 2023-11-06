package com.eric.fileshare.service;

import com.eric.fileshare.beans.User;
import com.eric.fileshare.dao.IUserDAO;
import com.eric.fileshare.dto.LoginDTO;
import com.eric.fileshare.dto.SignupDTO;
import com.eric.fileshare.exceptions.login.EmailNotFoundException;
import com.eric.fileshare.exceptions.login.LoginException;
import com.eric.fileshare.exceptions.login.PasswordIncorrectException;
import com.eric.fileshare.exceptions.signup.*;
import com.eric.fileshare.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class UserService implements IUserService{

    private IUserDAO userDAO;
    private RedisTemplate redisTemplate;

    public UserService(){}

    @Autowired
    public UserService(IUserDAO userDAO, RedisTemplate redisTemplate) {
        this.userDAO = userDAO;
        this.redisTemplate = redisTemplate;
    }

    /*
    * 用户登录的逻辑
    * 1. 在数据库中检查该用户是否存在
    *   1) 邮箱存在但是密码错误
    *   2) 邮箱不存在，未注册账户
    * */
    @Override
    public User doLogin(LoginDTO loginDTO) throws LoginException {
        User user = userDAO.findUserByEmail(loginDTO.getEmail());
        if(user == null) {
            throw new EmailNotFoundException("邮箱未注册");
        }else if(!user.getPassword().equals(EncryptionUtil.md5(loginDTO.getPassword()))) {
            throw new PasswordIncorrectException("密码错误");
        }
        return user;
    }

    /*
     * 用户注册的逻辑
     * 1. 在数据库中检查用户是否存在
     * 2. 校验用户的验证码
     * */
    @Override
    public void doSignup(SignupDTO signupDTO) throws SignupException {
        // 查询数据库中用户是否存在
        int count = userDAO.existEmail(signupDTO.getEmail());
        if(count == 0) {
            // 校验用户的验证码
            String code = (String)redisTemplate.opsForValue().get(signupDTO.getEmail());
            if(code == null) {
                throw new CodeExpirationException("验证码已过期, 请重新获取");
            }else if(!code.equals(signupDTO.getVerifyCode())) {
                throw new CodeIncorrectException("验证码不正确");
            }else{
                // 将用户信息加入数据库
                userDAO.insertUser(new User(null, UUID.randomUUID().toString(), signupDTO.getEmail(), signupDTO.getPassword(), new Timestamp(System.currentTimeMillis())));
            }
        }else{
            throw new EmailAlreadyInUseException("邮箱已被注册");
        }
    }

    @Override
    public long getBalance(String email) {
        long size = userDAO.getOccupiedSpaceByEmail(email);
        return 1 * 1024 * 1024 * 1024 - size;
    }


}
