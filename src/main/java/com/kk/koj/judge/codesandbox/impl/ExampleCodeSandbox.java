package com.kk.koj.judge.codesandbox.impl;

import cn.hutool.json.JSONUtil;
import com.kk.koj.judge.codesandbox.CodeSandbox;
import com.kk.koj.judge.codesandbox.model.ExecuteCodeRequest;
import com.kk.koj.judge.codesandbox.model.ExecuteCodeResponse;
import com.kk.koj.model.dto.judge.JudgeInfo;

/**
 * 示例代码沙箱
 * @author KK
 * @version 1.0
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(JSONUtil.toList("[\"0,1\", \"1,2\"]", String.class));
        executeCodeResponse.setMessage("执行正确");
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage("");
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);

        executeCodeResponse.setJudgeInfo(judgeInfo);
        
        return executeCodeResponse;
    }
}
