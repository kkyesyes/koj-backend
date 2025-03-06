package com.kk.koj.judge;

import cn.hutool.json.JSONUtil;
import com.kk.koj.common.ErrorCode;
import com.kk.koj.exception.BusinessException;
import com.kk.koj.judge.codesandbox.CodeSandbox;
import com.kk.koj.judge.codesandbox.CodeSandboxFactory;
import com.kk.koj.judge.codesandbox.model.ExecuteCodeRequest;
import com.kk.koj.judge.codesandbox.model.ExecuteCodeResponse;
import com.kk.koj.model.dto.judge.JudgeInfo;
import com.kk.koj.model.dto.problem.JudgeCase;
import com.kk.koj.model.dto.problem.JudgeConfig;
import com.kk.koj.model.entity.Problem;
import com.kk.koj.model.entity.Submit;
import com.kk.koj.model.enums.JudgeInfoEnum;
import com.kk.koj.model.enums.JudgeStatusEnum;
import com.kk.koj.service.ProblemService;
import com.kk.koj.service.SubmitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KK
 * @version 1.0
 */
@Service
public class JudgeServiceImpl implements JudgeService{

    private static final Logger log = LoggerFactory.getLogger(JudgeServiceImpl.class);
    @Resource
    private SubmitService submitService;

    @Resource
    private ProblemService problemService;

    @Value("${codesandbox.type}")
    private String codesandboxType;

    @Transactional
    @Override
    public void doJudge(long judgeId) {
        // 1) 根据id取得判题信息
        Submit submit = submitService.getById(judgeId);
        if (submit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交记录不存在");
        }

        // 2) 根据判题信息取得题目信息
        Long problemId = submit.getProblemId();
        Problem problem = problemService.getById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        Long id = submit.getId();
        String language = submit.getLanguage();
        String code = submit.getCode();

        // 避免重复判题
        if (submit.getStatus() != JudgeStatusEnum.WAITING.getValue()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "題目正在判題中");
        }

        // 更改判题状态为运行
        submit.setStatus(JudgeStatusEnum.RUNNING.getValue());
        boolean isUpdate = submitService.updateById(submit);
        if (!isUpdate) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "运行状态更改失败");
        }

        // 3) 执行判题
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setLanguage(submit.getLanguage());
        executeCodeRequest.setCode(submit.getCode());
        List<JudgeCase> judgeCaseList = JSONUtil.toList(problem.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream()
                .map(JudgeCase::getInput)
                .collect(Collectors.toList());

        executeCodeRequest.setInputList(inputList);

        // 4) 得到响应
        CodeSandbox codeSandbox = CodeSandboxFactory.getCodeSandbox(codesandboxType);
        ExecuteCodeResponse response = codeSandbox.execute(executeCodeRequest);

        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码沙箱执行失败");
        }
        List<String> outputList = response.getOutputList();
        log.info("[outputList]" + outputList);
        String message = response.getMessage();
        JudgeInfo resJudgeInfo = response.getJudgeInfo();

        List<String> caseOutputList = judgeCaseList.stream()
                .map(JudgeCase::getOutput)
                .collect(Collectors.toList());
        log.debug("[caseOutputList]" + outputList);


        // 根据沙箱结果判断
        // 判题结果
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoEnum.ACCEPTED.getText());


        // 输出结果数量错误
        if (caseOutputList.size() != outputList.size()) {
            judgeInfo.setMessage(JudgeInfoEnum.WRONG_ANSWER.getText());
            judgeInfo.setMessage(caseOutputList.toString());
            submit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
            submit.setStatus(JudgeStatusEnum.FAILED.getValue());
            submitService.updateById(submit);
            return;
        }

        // 输出结果数量正确
        // 逐个对比
        for (int i = 0; i < caseOutputList.size(); i++) {
            if (!outputList.get(i).equals(caseOutputList.get(i))) {
                judgeInfo.setMessage(JudgeInfoEnum.WRONG_ANSWER.getText());
                submit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
                submit.setStatus(JudgeStatusEnum.FAILED.getValue());
                submitService.updateById(submit);
                return;
            }
        }

        // 根据判题配置检查
        JudgeConfig judgeConfig = JSONUtil.toBean(problem.getJudgeConfig(), JudgeConfig.class);
        Long timeLimit = judgeConfig.getTimeLimit();
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long stackLimit = judgeConfig.getStackLimit();
        // 返回消耗最大资源
        judgeInfo.setMemory(resJudgeInfo.getMemory());
        judgeInfo.setTime(resJudgeInfo.getTime());
        // 时间超限
        if (resJudgeInfo.getTime() > timeLimit) {
            judgeInfo.setMessage(JudgeInfoEnum.TIME_EXCEED.getText());
            submit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
            submit.setStatus(JudgeStatusEnum.FAILED.getValue());
            submitService.updateById(submit);
            return;
        }
        // 内存超限
        if (resJudgeInfo.getMemory() > memoryLimit) {
            judgeInfo.setMessage(JudgeInfoEnum.MEMORY_EXCEED.getText());
            submit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
            submit.setStatus(JudgeStatusEnum.FAILED.getValue());
            submitService.updateById(submit);
            return;
        }

        submit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        submit.setStatus(JudgeStatusEnum.SUCCEED.getValue());
        submitService.updateById(submit);
    }
}
