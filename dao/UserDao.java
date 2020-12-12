package com.personal.dao;

import com.personal.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface UserDao {
    int deleteUser(String username);

    List<User> queryUsers();

    int insert(User user);

    int insertList(List<User> lists);

    User findByName(String username);


    int updateUser(User user);
}