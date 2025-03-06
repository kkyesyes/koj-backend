package com.kk.koj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码请求
 * @author KK
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeRequest {

    // 编程语言
    private String language;

    // 代码
    private String code;

    // 输入用例
    private List<String> inputList;

}
