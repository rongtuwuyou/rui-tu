package com.daowuyou.sessionservice.mapper;


import com.daowuyou.sessionservice.domain.po.Session;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话表 Mapper 接口
 *
 * @author uuy
 * @since 2025-02-02
 */
@Mapper
public interface SessionMapper extends MPJBaseMapper<Session> {

}
