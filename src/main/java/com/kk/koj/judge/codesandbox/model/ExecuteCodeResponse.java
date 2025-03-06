package com.kk.koj.judge.codesandbox.model;

import com.kk.koj.model.dto.judge.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码响应
 * @author KK
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {

    // 输出
    private List<String> outputList;

    // 执行代码信息
    private String message;

    // 判题信息
    private JudgeInfo judgeInfo;
}
