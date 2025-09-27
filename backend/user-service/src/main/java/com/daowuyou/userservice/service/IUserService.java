package com.daowuyou.userservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.daowuyou.userservice.domain.po.User;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author uuy
 * @since 2025-01-13
 */

public interface IUserService extends IService<User> {

    boolean register(User user);

    Map<String,Object> login(User user);

    boolean deleteUserById(Long userId);

    User updateUser(User user);
}
