package org.aidtracker.backend.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonBizException;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.util.SimpleResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Map;

/**
 * 全局异常处理
 * @author mtage
 * @since 2020/7/27 11:00
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonBizException.class)
    @ResponseBody
    public SimpleResult<?> handleCommonBizException(CommonBizException ex) throws UnknownHostException {
        return generateResult(ex.getErrorCode(), ex);
    }


    @ExceptionHandler(CommonSysException.class)
    @ResponseBody
    public SimpleResult<?> handleCommonSystemException(CommonSysException ex) throws UnknownHostException {
        return generateResult(ex.getErrorCode(), ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public SimpleResult<?> handleInvalidParameterException(IllegalArgumentException ex) throws UnknownHostException {
        return generateResult(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), ex);
    }

    private SimpleResult<Map<Object, Object>> generateResult(String code, Exception exception) throws UnknownHostException {
        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        return SimpleResult.fail(code,
                MessageFormat.format("{0} rootCauseMsg: {1} Host: {2}",
                        exception.getMessage(), rootCause.getMessage(), InetAddress.getLocalHost().getHostName()));
    }

}
