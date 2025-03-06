package com.kk.koj.model.dto.problem;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/likk">程序员鱼皮</a>
 * @from <a href="https://kk.icu">编程导航知识星球</a>
 */
@Data
public class ProblemUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（List 转 json 数组）
     */
    private List<String> tags;

    /**
     * 判题配置信息（json字符串）
     */
    private JudgeConfig judgeConfig;

    /**
     * 判题用例（json数组）
     */
    private List<JudgeCase> judgeCase;

    private static final long serialVersionUID = 1L;
}