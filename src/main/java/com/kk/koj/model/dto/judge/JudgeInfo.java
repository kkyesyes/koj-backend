package com.kk.koj.model.dto.judge;

import lombok.Data;

/**
 * 判题信息
 *
 * @author KK
 * @version 1.0
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 内存 (KB)
     */
    private Long memory;

    /**
     * 时间 (MS)
     */
    private Long time;
}
