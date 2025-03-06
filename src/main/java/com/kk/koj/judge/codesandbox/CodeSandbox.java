package com.kk.koj.judge.codesandbox;

import com.kk.koj.judge.codesandbox.model.ExecuteCodeRequest;
import com.kk.koj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口
 * @author KK
 * @version 1.0
 */
public interface CodeSandbox {

    ExecuteCodeResponse execute(ExecuteCodeRequest executeRequest);
}
