package com.daowuyou.sessionservice.controller;

import com.daowuyou.common.constant.UserConstant;
import com.daowuyou.common.domain.vo.ResultVO;
import com.daowuyou.sessionservice.domain.dto.SessionDTO;
import com.daowuyou.sessionservice.domain.po.Dialogue;
import com.daowuyou.sessionservice.domain.po.Session;
import com.daowuyou.sessionservice.domain.vo.PageVO;
import com.daowuyou.sessionservice.service.ISessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 会话服务
 * 名词解释:
 * session 会话: 一次总的聊天记录
 * dialogue 对话: 每一次的聊天记录，由一问一答多图片组成
 * session和dialogue的关系: 一个session包含多个dialogue
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/session")
public class SessionController {

    private final ISessionService sessionService;

    /**
     * 创建会话
     * 用户在创建对话并发起了第一次对话时调用此接口
     * 注意 ==!!不是!!点击了创建了一个会话就调用此接口(会出现空会话)==
     *
     * @param dialogue
     * @return
     */
    @PostMapping("/create_session")
    public ResultVO<Session> createSessionWith1stDialogue(@RequestBody Dialogue dialogue, @RequestHeader(UserConstant.USER_ID) Long userId) {
        log.info("user {}创建会话 第一次对话 {}", userId, dialogue);
        return ResultVO.success(
                sessionService.CreateSession(dialogue, userId)
        );
    }

    /**
     * 创建对话
     * 将会话的id、contentUser、contentAi, photoUrl(json字符数组)传递即可，其他不用管
     * 会话的id、contentUser不能为空，其他随意
     *
     * @param dialogue
     * @return
     */
    @PostMapping("/create_dialogue")
    public ResultVO createDialogue(@RequestBody Dialogue dialogue, @RequestHeader(UserConstant.USER_ID) Long userId) {
        log.info("user id = {},创建对话 {}", userId, dialogue);
        sessionService.createDialogue(dialogue);
        return ResultVO.success();
    }


    /**
     * 获取会话列表
     * 会话 + 前10个对话（对话排序为时间降序）
     * 按照创建时间降序排列
     *
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping("/get_session_list")
    public ResultVO<PageVO<SessionDTO>> getSessionList(
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize,
            @RequestHeader(UserConstant.USER_ID) Long userId
    ) {
        log.info("user id = {},获取会话列表 pageNum= {}, pageSize = {}", userId, pageNum, pageSize);
        return ResultVO.success(
                sessionService.getSessionListPage(pageNum, pageSize, userId, 5)
        );
    }


    /**
     * 分页获取会话详情
     * 传入sessionId，
     * 对话排序为时间降序
     *
     * @param sessionId
     * @return
     */
    @GetMapping("/get_session_detail")
    public ResultVO<PageVO<Dialogue>> getSessionDetailPage(
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize,
            @RequestParam Long sessionId,
            @RequestHeader(UserConstant.USER_ID) Long userId
    ) {
        log.info("user = {} 获取会话详情 sessionId = {} , pageNum = {}, pageSize = {}"
                , userId, sessionId, pageNum, pageSize);
        return ResultVO.success(
                sessionService.getSessionDetail(pageNum, pageSize, sessionId, userId)
        );
    }

    /**
     * 修改会话名称
     * Body 中传递 id 修改后的名字
     *
     * @param session
     * @param userId
     * @return
     */
    @PutMapping("/update_session_name")
    public ResultVO<Session> updateSessionName(@RequestBody Session session, @RequestHeader(UserConstant.USER_ID) Long userId) {
        log.info("user = {} 更新会话名称 {}", userId, session);
        return ResultVO.success(sessionService.updateSessionName(session, userId));
    }

    /**
     * 删除会话
     * 用户可以删除自己的会话，管理员可以删除任意一个人的
     *
     * @param sessionId
     * @param userId
     * @param identity
     * @return
     */
    @DeleteMapping("/delete_session/{sessionId}")
    public ResultVO deleteSession(
            @PathVariable Long sessionId,
            @RequestHeader(UserConstant.USER_ID) Long userId,
            @RequestHeader(UserConstant.IDENTITY) Integer identity
    ) {
        log.info("user = {} 删除会话 sessionId = {} , identity = {}", userId, sessionId, identity);
        boolean b = sessionService.deleteSessionById(sessionId, userId, identity);
        return b ? ResultVO.success() : ResultVO.error("删除失败");
    }

    /**
     * 更新对话点赞状态
     * 点赞状态: 0 未点赞 | 1 点赞 | 2 不喜欢
     * @param dialogue
     * @param userId
     * @return
     */
    @PutMapping("/dialogue/update_like_status")
    public ResultVO<Dialogue> updateLikeStatus(@RequestBody Dialogue dialogue, @RequestHeader(UserConstant.USER_ID) Long userId) {
        log.info("user = {} 更新对话点赞状态 {}", userId, dialogue);
        return ResultVO.success(sessionService.updateDialogueLikeStatus(dialogue, userId));
    }
}