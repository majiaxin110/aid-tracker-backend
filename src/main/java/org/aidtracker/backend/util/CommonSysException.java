package org.aidtracker.backend.util;

import lombok.Getter;

/**
 * @author mtage
 * @date 2019/10/4 14:50
 **/
public class CommonSysException extends RuntimeException {
    private static final long serialVersionUID = -1525668676119894006L;
    @Getter
    private String errorCode;
    private String message;

    /**
     * Constructs a {@code CommonSysException} with no detail message.
     */
    public CommonSysException() {
    }

    /**
     * Constructs a {@code CommonSysException} with the specified
     * detail message.
     *
     * @param message the detail message.
     */
    public CommonSysException(String errorCode, String message) {
        super(String.format("code:%s msg:%s", errorCode, message));
        this.errorCode = errorCode;
        this.message = message;
    }

    public CommonSysException(String errorCode, String message, Throwable cause) {
        super(String.format("code:%s msg:%s", errorCode, message), cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
