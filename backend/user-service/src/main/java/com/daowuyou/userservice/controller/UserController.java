package com.daowuyou.userservice.controller;


import cn.hutool.core.bean.BeanUtil;
import com.daowuyou.common.constant.UserConstant;
import com.daowuyou.common.domain.dto.UserDTO;
import com.daowuyou.common.domain.vo.ResultVO;
import com.daowuyou.userservice.domain.po.User;
import com.daowuyou.userservice.exception.LogicException;
import com.daowuyou.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.daowuyou.common.domain.vo.ResultVO.success;

/**
 * 用户服务
 *
 * @author uuy
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;
    /**
     * 注册用户
     * 哈哈哈哈
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResultVO<User> register(@RequestBody User user) {
        log.info("register user: {}", user);
        return userService.register(user) ? success() : ResultVO.error("注册失败");
    }


    /**
     * 登录
     * 返回token 和 用户信息
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResultVO<Object> login(@RequestBody User user) {
        log.info("login user: {}", user);
        Map<String, Object> res = userService.login(user);
        return res != null && !res.isEmpty() ?
                ResultVO.success(res)
                :
                ResultVO.error("登录失败");
    }

    /**
     * 注销账户
     * @param userId
     * @return
     */
    @DeleteMapping("/delete")
    public ResultVO<User> delete(@RequestHeader(UserConstant.USER_ID) Long userId) {
        log.info("注册用户 {}", userId);
        return  userService.deleteUserById(userId) ?
                ResultVO.success() : ResultVO.error("注销失败");
    }
    /**
     * 修改用户信息
     * @param user
     * @param userId
     * @return
     */
    @PutMapping("/update")
    public ResultVO<User> update(@RequestBody User user, @RequestHeader(UserConstant.USER_ID) Long userId) {
        log.info("修改用户信息 {}", user);
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return updatedUser != null ? ResultVO.success(updatedUser) : ResultVO.error("修改失败");
    }


    /**
     * 获取用户信息byId
     * 微服务内调用
     * @param userId
     * @return
     */
    @GetMapping("/getUserById")
    public ResultVO<UserDTO> getUserById(@RequestParam Long userId) {
        log.info("获取用户信息 userid {}", userId);
        User user = userService.getOptById(userId).orElseThrow(() -> new LogicException("无此user信息"));
        user.setPassword(UserConstant.PWD_MASK);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        return ResultVO.success(userDTO);
    }
}