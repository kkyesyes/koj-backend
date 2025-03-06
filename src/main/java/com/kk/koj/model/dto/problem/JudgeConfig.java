package com.kk.koj.model.dto.problem;

import lombok.Data;

/**
 * @author KK
 * @version 1.0
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制 (ms)
     */
    private Long timeLimit;

    /**
     * 内存限制 (KB)
     */
    private Long memoryLimit;

    /**
     * 堆栈限制 (KB)
     */
    private Long stackLimit;
}
