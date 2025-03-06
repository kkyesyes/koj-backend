package com.kk.koj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.koj.common.BaseResponse;
import com.kk.koj.common.ErrorCode;
import com.kk.koj.common.ResultUtils;
import com.kk.koj.exception.BusinessException;
import com.kk.koj.exception.ThrowUtils;
import com.kk.koj.model.dto.judge.SubmitAddRequest;
import com.kk.koj.model.dto.judge.SubmitQueryRequest;
import com.kk.koj.model.entity.Submit;
import com.kk.koj.model.entity.User;
import com.kk.koj.model.vo.JudgeVO;
import com.kk.koj.service.SubmitService;
import com.kk.koj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/likk">程序员鱼皮</a>
 * @from <a href="https://kk.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/judge")
@Slf4j
public class JudgeController {

    @Resource
    private SubmitService judgeService;

    @Resource
    private UserService userService;

    /**
     * 提交判题
     *
     * @param judgeAddRequest
     * @param request
     * @return resultNum
     */
    @PostMapping("/")
    public BaseResponse<Long> submitJudge(@RequestBody SubmitAddRequest judgeAddRequest,
                                         HttpServletRequest request) {
        // 题目是否有效
        if (judgeAddRequest == null || judgeAddRequest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        long problemId = judgeAddRequest.getProblemId();
        long judgeId = judgeService.submit(judgeAddRequest, loginUser);

        return ResultUtils.success(judgeId);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Submit> getJudgeById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Submit judge = judgeService.getById(id);
        if (judge == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(judge);
    }

    /**
     * 分页获取列表 judgeVO
     *
     * @param judgeQueryRequest
     * @return
     */
    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Submit>> listJudgeByPage(@RequestBody SubmitQueryRequest judgeQueryRequest, HttpServletRequest request) {
        long current = judgeQueryRequest.getCurrent();
        long size = judgeQueryRequest.getPageSize();
        Page<Submit> judgePage = judgeService.page(new Page<>(current, size),
                judgeService.getQueryWrapper(judgeQueryRequest));
        return ResultUtils.success(judgePage);
    }

    /**
     * 分页获取当前用户提交的判题列表
     *
     * @param judgeQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<JudgeVO>> listMyJudgeVOByPage(@RequestBody SubmitQueryRequest judgeQueryRequest,
                                                           HttpServletRequest request) {
        if (judgeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        judgeQueryRequest.setUserId(loginUser.getId());
        long current = judgeQueryRequest.getCurrent();
        long size = judgeQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Submit> problemPage = judgeService.page(new Page<>(current, size),
                judgeService.getQueryWrapper(judgeQueryRequest));
        return ResultUtils.success(judgeService.getJudgeVOPage(problemPage, request));
    }

}
