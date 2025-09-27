package com.daowuyou.sessionservice.service;


import com.daowuyou.sessionservice.domain.dto.SessionDTO;
import com.daowuyou.sessionservice.domain.po.Dialogue;
import com.daowuyou.sessionservice.domain.po.Session;
import com.daowuyou.sessionservice.domain.vo.PageVO;
import com.github.yulichang.base.MPJBaseService;

/**
 * <p>
 * 会话表 服务类
 * </p>
 *
 * @author uuy
 * @since 2025-02-02
 */
public interface ISessionService extends MPJBaseService<Session> {

    Session CreateSession(Dialogue dialogue, Long userId);

    void createDialogue(Dialogue dialogue);

    PageVO<SessionDTO> getSessionListPage(Long pageNum, Long pageSize, Long userId,int maxDialogueNum);
    Session updateSessionName(Session session, Long userId);
    PageVO<Dialogue> getSessionDetail(Long pageNum, Long pageSize, Long sessionId, Long userId);

    boolean deleteSessionById(Long sessionId, Long userId, Integer identity);

    Dialogue updateDialogueLikeStatus(Dialogue dialogue, Long userId);
}
