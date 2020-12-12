package com.personal.service;

import com.personal.entity.User;

import java.util.List;

/**
 * 描述：用户表服务类
 * @author
 * @date 2020/06/24 15:43
 **/
public interface UserService {
    /**
     * 根据用户名查信息
     *
     * @param username
     * @return
     */
    User findByName(String username);

    /**
     * 查询所有的用户
     */
    List<User> queryUsers();

    /**
     * 更新
     */
    int updateUser(User user);

    /**
     * 新增用户
     */
    int insert(User user);

    /**
     * 删除用户
     */
    int deleteUser(String username);

    /**
     * 批量插入
     */
    int insertList(List<User> lists);

}
