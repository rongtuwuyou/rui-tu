package com.daowuyou.sessionservice.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 会话表
 *
 * @author uuy
 * @since 2025-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("session")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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

}
