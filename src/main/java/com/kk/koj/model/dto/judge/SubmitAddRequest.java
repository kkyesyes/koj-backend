package com.kk.koj.model.dto.judge;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子点赞请求
 *
 * @author <a href="https://github.com/likk">程序员鱼皮</a>
 * @from <a href="https://kk.icu">编程导航知识星球</a>
 */
@Data
public class SubmitAddRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long problemId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;

    private static final long serialVersionUID = 1L;
}