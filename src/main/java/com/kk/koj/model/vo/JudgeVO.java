package com.kk.koj.model.vo;

import com.kk.koj.model.entity.Submit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 判题视图（脱敏）
 */
@Data
public class JudgeVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 对象）
     */
    private String judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private String status;

    /**
     * 题目 id
     */
    private Long problemId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param judge
     * @return
     */
    public static JudgeVO objToVo(Submit judge) {
        if (judge == null) {
            return null;
        }
        JudgeVO judgeVO = new JudgeVO();
        BeanUtils.copyProperties(judge, judgeVO);
        //  转 status 为 Enum
        if (judge.getStatus() != null) {
            switch (judge.getStatus()) {
                case 0:
                    judgeVO.setStatus("待判题");
                    break;
                case 1:
                    judgeVO.setStatus("判题中");
                    break;
                case 2:
                    judgeVO.setStatus("判题正确");
                    break;
                case 3:
                    judgeVO.setStatus("判题错误");
                    break;
                default:
                    break;
            }
        }
        return judgeVO;
    }
}