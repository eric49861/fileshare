package com.eric.fileshare.dao;

import com.eric.fileshare.beans.User;

public interface IUserDAO {

    void insertUser(User user);

    User findUserByEmail(String email);

    int existEmail(String email);

    long getOccupiedSpaceByEmail(String email);
}
