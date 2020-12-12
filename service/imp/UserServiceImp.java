package com.personal.service.imp;

import com.personal.dao.UserDao;
import com.personal.entity.User;
import com.personal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User findByName(String username) {
        return userDao.findByName(username);
    }

    @Override
    public List<User> queryUsers() {
        return userDao.queryUsers();
    }

    @Override
    public int updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public int insert(User user) {
        return userDao.insert(user);
    }

    @Override
    public int deleteUser(String username) {
        return userDao.deleteUser(username);
    }

    @Override
    public int insertList(List<User> lists) {
        return userDao.insertList(lists);
    }
}
