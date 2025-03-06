package com.kk.koj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.koj.model.dto.judge.SubmitAddRequest;
import com.kk.koj.model.dto.judge.SubmitQueryRequest;
import com.kk.koj.model.entity.Submit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.koj.model.entity.User;
import com.kk.koj.model.vo.JudgeVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author SK
* @description 针对表【judge(判题)】的数据库操作Service
* @createDate 2025-02-15 17:41:02
*/
public interface SubmitService extends IService<Submit> {

    long submit(SubmitAddRequest judgeAddRequest, User loginUser);

    QueryWrapper<Submit> getQueryWrapper(SubmitQueryRequest judgeQueryRequest);

    Page<JudgeVO> getJudgeVOPage(Page<Submit> judgePage, HttpServletRequest request);

}
