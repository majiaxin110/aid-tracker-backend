package org.aidtracker.backend.util;

import lombok.Getter;

/**
 * @author mtage
 * @since 2020/7/27 11:01
 */
public enum AidTrackerCommonErrorCode {
    /**
     * 目标实体未找到
     */
    NOT_FOUND("NOT_FOUND"),

    FORBIDDEN("FORBIDDEN"),

    /**
     * 参数错误
     */
    INVALID_PARAM("INVALID_PARAM"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR("SYSTEM_ERROR"),

    /**
     * 对象存储连接异常
     */
    COS_ERROR("COS_ERROR"),

    /**
     * 配置错误
     */
    CONFIG_ERROR("CONFIG_ERROR")

    ;

    @Getter
    private String errorCode;

    AidTrackerCommonErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
