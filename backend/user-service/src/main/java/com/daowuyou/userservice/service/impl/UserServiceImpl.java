package com.daowuyou.userservice.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.daowuyou.common.constant.UserConstant;
import com.daowuyou.common.enums.IdentityEnums;
import com.daowuyou.userservice.domain.po.User;
import com.daowuyou.userservice.exception.LogicException;
import com.daowuyou.userservice.mapper.UserMapper;
import com.daowuyou.userservice.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daowuyou.userservice.utils.JWTUtils;
import com.daowuyou.userservice.utils.MD5Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author uuy
 * @since 2025-01-13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final JWTUtils jwtUtils;

    @Override
    public boolean register(User user) {
        //数据检查
        checkUserNamePwd(user);
        user.setPassword(MD5Utils.toMD5(user.getPassword()));
        //默认普通用户
        if(user.getIdentity() == null){
            user.setIdentity(IdentityEnums.NORMAL_USER.getCode());
        }
        try {
            return save(user);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            log.error("用户名重复");
            throw new LogicException("用户名重复");
        } catch (Exception e) {
            log.error("用户注册失败");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Object> login(User user) {
        checkUserNamePwd(user);
        User userInfo = Optional.ofNullable(
                new LambdaQueryChainWrapper<>(userMapper)
                        .eq(User::getUsername, user.getUsername())
                        .eq(User::getPassword, MD5Utils.toMD5(user.getPassword()))
                        .one()
        ).orElseThrow(() -> new LogicException("用户名或密码错误"));
        //数据处理
        //  脱敏
        userInfo.setPassword(UserConstant.PWD_MASK);
        //  无nickname
        if(StrUtil.isBlank(userInfo.getNickname())){
            userInfo.setNickname(UserConstant.DEFAULT_NICK_NAME_PREFIX + (userInfo.getId() + 100));
        }
        //jwt
        try {
            Map<String, Object> claim = new ConcurrentHashMap<>();
            claim.put(UserConstant.USER_ID, userInfo.getId());
            claim.put(UserConstant.IDENTITY, userInfo.getIdentity());
            String token = jwtUtils.generateJwt(claim);
            // 封装返回
            return Map.of("userInfo", userInfo, "token", token);
        } catch (Exception e) {
            log.error("生成token失败", e);
            throw new LogicException("生成token失败");
        }
    }

    @Override
    public boolean deleteUserById(Long userId) {
        int delete = userMapper.delete(
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, userId)
        );
        return delete != 0;
    }

    @Override
    public User updateUser(User user) {
        user.setUpdatedTime(null);
        user.setCreatedTime(null);
        int update = userMapper.update(user, new LambdaUpdateWrapper<User>().eq(User::getId, user.getId()));
        if(update == 0){
            return null;
        }
        user = new LambdaQueryChainWrapper<>(userMapper).eq(User::getId, user.getId()).one();
        user.setPassword(UserConstant.PWD_MASK);
        return user;
    }

    private boolean checkUserNamePwd(User user) {
        Optional.ofNullable(user).orElseThrow(() -> new LogicException("传入user为空"));
        Optional.ofNullable(user.getUsername()).orElseThrow(() -> new LogicException("用户名为空"));
        Optional.ofNullable(user.getPassword()).orElseThrow(() -> new LogicException("密码为空"));
        return true;
    }
}