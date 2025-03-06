package com.kk.koj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.koj.common.ErrorCode;
import com.kk.koj.constant.CommonConstant;
import com.kk.koj.exception.BusinessException;
import com.kk.koj.judge.JudgeService;
import com.kk.koj.mapper.JudgeMapper;
import com.kk.koj.model.dto.judge.SubmitAddRequest;
import com.kk.koj.model.dto.judge.SubmitQueryRequest;
import com.kk.koj.model.entity.Submit;
import com.kk.koj.model.entity.Problem;
import com.kk.koj.model.entity.User;
import com.kk.koj.model.enums.LanguageEnum;
import com.kk.koj.model.vo.JudgeVO;
import com.kk.koj.service.SubmitService;
import com.kk.koj.service.ProblemService;
import com.kk.koj.utils.SqlUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.CompletableFuture;

import static com.kk.koj.model.enums.JudgeStatusEnum.WAITING;

/**
 * @author SK
 * @description 针对表【judge(判题)】的数据库操作Service实现
 * @createDate 2025-02-15 17:41:02
 */
@Service
public class SubmitServiceImpl extends ServiceImpl<JudgeMapper, Submit>
        implements SubmitService {

    @Resource
    private ProblemService problemService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    @Transactional
    @Override
    public long submit(SubmitAddRequest judgeAddRequest, User loginUser) {
        // 解构对象
        Long problemId = judgeAddRequest.getProblemId();
        String language = judgeAddRequest.getLanguage();
        String code = judgeAddRequest.getCode();

        // 检查编程语言
        LanguageEnum languageEnum = LanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        // 检查题目
        Problem problem = problemService.getById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 检查用户
        Long userId = loginUser.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        Submit submit = new Submit();
        submit.setUserId(userId);
        submit.setProblemId(problemId);
        submit.setLanguage(language);
        submit.setCode(code);
        submit.setStatus(WAITING.getValue());
        submit.setJudgeInfo("{}");

        // 返回提交记录 id
        boolean save = this.save(submit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题记录录入失败");
        }

        // 提交数增加
        problemService.incSubmitNumById(problem);

        // 异步提交
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(submit.getId());
        });

        // 返回记录 id
        return submit.getId();
    }

    /**
     * 获取查询包装类
     *
     * @param judgeQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Submit> getQueryWrapper(SubmitQueryRequest judgeQueryRequest) {
        QueryWrapper<Submit> queryWrapper = new QueryWrapper<>();
        if (judgeQueryRequest == null) {
            return queryWrapper;
        }

        String sortField = judgeQueryRequest.getSortField();
        String sortOrder = judgeQueryRequest.getSortOrder();
        Long id = judgeQueryRequest.getId();
        String language = judgeQueryRequest.getLanguage();
        String code = judgeQueryRequest.getCode();
        String judgeInfo = judgeQueryRequest.getJudgeInfo();
        Integer status = judgeQueryRequest.getStatus();
        Long problemId = judgeQueryRequest.getProblemId();
        Long userId = judgeQueryRequest.getUserId();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.like(StringUtils.isNotBlank(code), "code", code);
        queryWrapper.like(StringUtils.isNotBlank(judgeInfo), "judgeInfo", judgeInfo);
        queryWrapper.like(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(problemId), "problemId", problemId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    // todo 未做，暂无必要
    @Override
    public Page<JudgeVO> getJudgeVOPage(Page<Submit> judgePage, HttpServletRequest request) {
        return null;
    }
}




