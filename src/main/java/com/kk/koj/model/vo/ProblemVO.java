package com.kk.koj.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kk.koj.model.dto.problem.JudgeConfig;
import com.kk.koj.model.entity.Post;
import com.kk.koj.model.entity.Problem;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图（脱敏）
 */
@Data
public class ProblemVO implements Serializable {
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
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 判题配置信息（json字符串）
     */
    private JudgeConfig judgeConfig;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * ac数
     */
    private Integer acceptedNum;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param problem
     * @return
     */
    public static ProblemVO objToVo(Problem problem) {
        if (problem == null) {
            return null;
        }
        ProblemVO problemVO = new ProblemVO();
        BeanUtils.copyProperties(problem, problemVO);
        //  转 tags 为 List
        if (problem.getTags() != null) {
            problemVO.setTags(JSONUtil.toList(problem.getTags(), String.class));
        }
        // 转 String 为 JudgeConfig
        JudgeConfig judgeConfig = JSONUtil.toBean(problem.getJudgeConfig(), JudgeConfig.class);
        problemVO.setJudgeConfig(judgeConfig);
        return problemVO;
    }

    /**
     * 包装类转对象
     *
     * @param problemVO
     * @return
     */
    public static Problem voToObj(ProblemVO problemVO) {
        if (problemVO == null) {
            return null;
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemVO, problem);
        // 转 tags 为 String
        List<String> tagList = problemVO.getTags();
        if (tagList != null) {
            problem.setTags(JSONUtil.toJsonStr(tagList));
        }
        // 转 judgeConfig 为 String
        JudgeConfig voJudgeConfig = problemVO.getJudgeConfig();
        if (voJudgeConfig != null) {
            problem.setJudgeConfig(JSONUtil.toJsonStr(voJudgeConfig));
        }

        return problem;
    }
}