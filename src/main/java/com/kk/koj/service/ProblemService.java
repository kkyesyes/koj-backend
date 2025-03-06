package com.kk.koj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.koj.model.dto.problem.ProblemQueryRequest;
import com.kk.koj.model.entity.Post;
import com.kk.koj.model.entity.Problem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.koj.model.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author SK
* @description 针对表【problem(题目)】的数据库操作Service
* @createDate 2025-02-15 12:43:18
*/
public interface ProblemService extends IService<Problem> {

    void validProblem(Problem problem, boolean add);

    QueryWrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest);

    ProblemVO getProblemVO(Problem problem, HttpServletRequest request);

    Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, HttpServletRequest request);

    boolean incSubmitNumById(Problem problem);
}
