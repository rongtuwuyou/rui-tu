package com.daowuyou.sessionservice.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daowuyou.common.enums.IdentityEnums;
import com.daowuyou.sessionservice.domain.dto.SessionDTO;
import com.daowuyou.sessionservice.domain.po.Dialogue;
import com.daowuyou.sessionservice.domain.po.Session;
import com.daowuyou.sessionservice.domain.vo.PageVO;
import com.daowuyou.sessionservice.exception.LogicException;
import com.daowuyou.sessionservice.mapper.DialogueMapper;
import com.daowuyou.sessionservice.mapper.SessionMapper;
import com.daowuyou.sessionservice.service.ISessionService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会话表 服务实现类
 *
 * @author uuy
 * @since 2025-02-02
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SessionServiceImpl extends MPJBaseServiceImpl<SessionMapper, Session> implements ISessionService {

    private final SessionMapper sessionMapper;
    private final DialogueMapper dialogueMapper;


    @Override
    public Session CreateSession(Dialogue dialogue, Long userId) {
        //创建会话
        Session session = new Session();
        session.setUserId(userId);
        session.setStartTime(LocalDateTime.now());
        String SessionName = Optional.ofNullable(dialogue).map(Dialogue::getContentUser).orElseThrow(() -> new LogicException("第一次对话不能为空"));
        session.setSessionName(SessionName);
        Integer status = Optional.of(dialogue).map(Dialogue::getStatus).orElse(0);
        session.setStatus(status);

        if(CollUtil.isEmpty(dialogue.getPhotoUrl())){
            dialogue.setPhotoUrl(new ArrayList<String>());
        }

        if(CollUtil.isEmpty(dialogue.getPhotoUrlAi())){
            dialogue.setPhotoUrlAi(new ArrayList<String>());
        }
        sessionMapper.insert(session);
        //

        dialogue.setSessionId(
                Optional.ofNullable(session).map(Session::getId).orElseThrow(() -> new LogicException("会话创建失败, 无法获取SessionId"))
        );
        dialogue.setCreateTime(LocalDateTime.now());
        dialogueMapper.insert(dialogue);

        return session;
    }

    @Override
    public void createDialogue(Dialogue dialogue) {
        //数据校验
        Optional.ofNullable(dialogue).map(Dialogue::getSessionId).orElseThrow(() -> new LogicException("会话ID不能为空"));
        Optional.of(dialogue).map(Dialogue::getContentUser).orElseThrow(() -> new LogicException("对话内容不能为空"));
        if(CollUtil.isEmpty(dialogue.getPhotoUrl())){
            dialogue.setPhotoUrl(new ArrayList<String>());
        }

        if(CollUtil.isEmpty(dialogue.getPhotoUrlAi())){
            dialogue.setPhotoUrlAi(new ArrayList<String>());
        }

        dialogue.setCreateTime(LocalDateTime.now());
        dialogueMapper.insert(dialogue);
    }

    @Override
    public PageVO<SessionDTO> getSessionListPage(Long pageNum, Long pageSize, Long userId,int maxDialogueNum) {
        Optional.ofNullable(userId).orElseThrow(() -> new LogicException("身份校验错误"));


//        Page<SessionDTO> doPage = sessionMapper.selectJoinPage(
//                new Page<>(pageNum, pageSize),
//                SessionDTO.class,
//                JoinWrappers.lambda(Session.class)
//                        .selectAll(Session.class)
//                        .selectCollection(Dialogue.class, SessionDTO::getDialogueList)
//                        .leftJoin(Dialogue.class, on -> on
//                                .inSql(Dialogue::getId,
//                                        "SELECT id FROM ( " +
//                                                "    SELECT id, session_id, ROW_NUMBER() OVER (PARTITION BY session_id ORDER BY create_time DESC) AS rn " +
//                                                "    FROM dialogue " +
//                                                ") t WHERE t.rn <= 10"
//                                ).eq(Dialogue::getSessionId, Session::getId)
//                        )
//                        .eq(Session::getUserId, userId)
//                        .orderByDesc(Session::getStartTime)
//        );

//        sessionMapper.page

        Page<Session> sessionPage = sessionMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<>(Session.class)
                        .eq(Session::getUserId, userId)
                        .orderByDesc(Session::getStartTime)
        );
        List<Session> sessionRecords = sessionPage.getRecords();

        if(CollUtil.isEmpty(sessionRecords) &&
                sessionPage.getSize() * sessionPage.getCurrent() - 1 > sessionPage.getTotal()
        ){
            return new PageVO<SessionDTO>(
                    sessionPage.getCurrent()
                    , sessionPage.getSize()
                    , sessionPage.getTotal()
                    , null
            );
        }


        List<Long> sessionIds = new LinkedList<>();
        for (Session sessionRecord : sessionRecords) {
            sessionIds.add(sessionRecord.getId());
        }
        if(sessionIds.size() == 0){
            throw new LogicException("会话列表为空, 请稍后重试");
        }

        List<Dialogue> dialogues = dialogueMapper.selectList(
                new LambdaQueryWrapper<>(Dialogue.class)
                        .in(Dialogue::getSessionId, sessionIds)
                        .orderByDesc(Dialogue::getCreateTime)
                        .last("limit " + maxDialogueNum)
        );

        //根据sessionId分组
        Map<Long, List<Dialogue>> dialogueMap = dialogues.stream()
                .collect(Collectors.groupingBy(Dialogue::getSessionId));

        List<SessionDTO> sessionDTOList = new LinkedList<>();
        for (Session sessionRecord : sessionRecords) {
            SessionDTO sessionDTO = BeanUtil.copyProperties(sessionRecord, SessionDTO.class);
            sessionDTO.setDialogueList(dialogueMap.get(sessionRecord.getId()));
            sessionDTOList.add(sessionDTO);
        }

        return new PageVO<SessionDTO>(
                sessionPage.getCurrent()
                , sessionPage.getSize()
                , sessionPage.getTotal()
                , sessionDTOList
        );
    }

    @Override
    public PageVO<Dialogue> getSessionDetail(Long pageNum, Long pageSize, Long sessionId, Long userId) {
        Optional.ofNullable(sessionId).orElseThrow(() -> new LogicException("sessionId 会话ID不能为空"));
        Page<Dialogue> doPage = dialogueMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<>(Dialogue.class)
                        .eq(Dialogue::getSessionId, sessionId)
                        .orderByDesc(Dialogue::getCreateTime)
        );
        return new PageVO<Dialogue>(
                doPage.getCurrent()
                , doPage.getSize()
                , doPage.getTotal()
                , doPage.getRecords()
        );
    }

    /**
     * 用户可以删除自己的会话，管理员可以删除任意一个人的
     * @param sessionId
     * @param userId
     * @param identity
     */
    @Override
    public boolean deleteSessionById(Long sessionId, Long userId, Integer identity) {
        Optional.ofNullable(sessionId).orElseThrow(() -> new LogicException("sessionId 会话ID不能为空"));
        Integer flag = 0;

        switch (IdentityEnums.fromCode(identity)){
            case ADMIN->{
                flag = sessionMapper.deleteById(sessionId);
                if(flag != 0){
                    dialogueMapper.delete(
                            new LambdaQueryWrapper<>(Dialogue.class)
                                    .eq(Dialogue::getSessionId, sessionId));
                }
            }
            case NORMAL_USER->{
                flag = sessionMapper.delete(
                        new LambdaQueryWrapper<>(Session.class)
                                .eq(Session::getUserId, userId)
                                .eq(Session::getId, sessionId)
                );
                if(flag != 0){
                    dialogueMapper.delete(new LambdaQueryWrapper<>(Dialogue.class)
                            .eq(Dialogue::getSessionId, sessionId)
                    );
                }
            }
        }
        return flag != 0;

    }

    @Override
    public Dialogue updateDialogueLikeStatus(Dialogue dialogue, Long userId) {

        Optional.ofNullable(dialogue).map(Dialogue::getId).orElseThrow(() -> new LogicException("dialogue 中 id为空"));
        dialogueMapper.update(dialogue,
                new LambdaQueryWrapper<>(Dialogue.class)
                       .eq(Dialogue::getId, dialogue.getId())
        );
        return  dialogueMapper.selectById(dialogue.getId());

    }

    @Override
    public Session updateSessionName(Session session, Long userId) {

        Optional.ofNullable(session).map(Session::getId).orElseThrow(() -> new LogicException("session 中 id为空"));
        if (StrUtil.isBlank(session.getSessionName())){
            throw new LogicException("会话名称不能为空");
        }
        int update = sessionMapper.update(session,
                new LambdaQueryWrapper<>(Session.class)
                        .eq(Session::getUserId, userId)
                        .eq(Session::getId, session.getId())
        );
        if(update == 0){
            throw new LogicException("会话未找到,会话名称修改失败");
        }
        return sessionMapper.selectOne(
                new LambdaQueryWrapper<>(Session.class)
                        .eq(Session::getId, session.getId())
        );
    }
}