package com.daowuyou.userservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daowuyou.userservice.domain.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 *  Mapper 接口
 * @author uuy
 * @since 2025-01-13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
