package com.daowuyou.sessionservice.domain.dto;

import com.daowuyou.sessionservice.domain.po.Dialogue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDTO {

    private Long id;

    /**
     * 会话名称
     */
    private String sessionName;

    private Long userId;

    /**
     * 会话开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间-可选
     */
    private LocalDateTime endTime;

    /**
     * 扩展备用字段
     */
    private String extension;


    private Integer status;
    
    // 对话列表
    private List<Dialogue> dialogueList;
    
}
