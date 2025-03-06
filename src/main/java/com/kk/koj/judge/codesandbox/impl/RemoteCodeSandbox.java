package com.kk.koj.judge.codesandbox.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kk.koj.common.ErrorCode;
import com.kk.koj.exception.BusinessException;
import com.kk.koj.judge.codesandbox.CodeSandbox;
import com.kk.koj.judge.codesandbox.model.ExecuteCodeRequest;
import com.kk.koj.judge.codesandbox.model.ExecuteCodeResponse;
import net.bytebuddy.implementation.bytecode.Throw;

/**
 * @author KK
 * @version 1.0
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeRequest) {

        String url = "http://localhost:8091/judge";
        String jsonStr = JSONUtil.toJsonStr(executeRequest);
        String response = HttpUtil.createPost(url)
                .body(jsonStr)
                .execute()
                .body();
        if (StringUtils.isBlank(response)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "remote-code-sandbox error" + response);
        }
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(response, ExecuteCodeResponse.class);
        return executeCodeResponse;
    }
}
