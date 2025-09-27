package com.daowuyou.sessionservice.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 对话表
 * @author uuy
 * @since 2025-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "dialogue", autoResultMap = true)
public class Dialogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联session
     */
    private Long sessionId;

    /**
     * 用户说的话
     */
    private String contentUser;

    /**
     * ai回答的话
     */
    private String contentAi;

    /**
     * 图片路径
     */
    @TableField(value = "photo_url",typeHandler = JacksonTypeHandler.class)
    private List<String> photoUrl;


    /**
     * 图片路径
     */
    @TableField(value = "photo_url_ai",typeHandler = JacksonTypeHandler.class)
    private List<String> photoUrlAi;

    /**
     * 点赞状态: 0 未点赞 | 1 点赞 | 2 不喜欢
     */
    private Integer likeStatus;

    /**
     * 对话创建时间
     */
    private LocalDateTime createTime;

    /**
     * 扩展备用字段
     */
    private String extension;

    private Integer status;
}
