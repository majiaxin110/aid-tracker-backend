package org.aidtracker.backend.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 通用的API包装
 *
 * @author mtage
 * @since 2020/2/7 20:40
 */
@Data
public class SimpleResult<T> implements Serializable {
    private static final long serialVersionUID = -592650298251475150L;

    /**
     * 该errorCode旨在满足规范错误码的需要，与Http状态码并不冲突
     */
    private String errorCode;

    private String errorMsg;

    private Boolean success;

    private ZonedDateTime time = ZonedDateTime.now();

    private T result;

    private static final String OK = "OK";

    public static <T> SimpleResult<T> success(T result) {
        SimpleResult<T> simpleResult = new SimpleResult<>();
        simpleResult.setSuccess(true);
        simpleResult.setResult(result);
        return simpleResult;
    }

    public static SimpleResult<String> ok() {
        return success(OK);
    }

    public static <T> SimpleResult<T> fail(String errorCode, String errorMsg) {
        SimpleResult<T> simpleResult = new SimpleResult<>();
        simpleResult.setSuccess(false);
        simpleResult.setErrorCode(errorCode);
        simpleResult.setErrorMsg(errorMsg);
        return simpleResult;
    }

}
