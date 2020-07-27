package org.aidtracker.backend.web.exception;

import com.google.common.collect.Maps;
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
import java.security.InvalidParameterException;
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
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        SimpleResult<Map<Object, Object>> simpleResult = SimpleResult.fail(ex.getErrorCode(),
                MessageFormat.format("{0} rootCauseMsg: {1} Host: {2}",
                        ex.getMessage(), rootCause.getMessage(), InetAddress.getLocalHost().getHostName()));
        simpleResult.setResult(Maps.newHashMap());
        return simpleResult;
    }


    @ExceptionHandler(CommonSysException.class)
    @ResponseBody
    public SimpleResult<?> handleCommonSystemException(CommonSysException ex) throws UnknownHostException {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        SimpleResult<Map<Object, Object>> simpleResult = SimpleResult.fail(ex.getErrorCode(),
                MessageFormat.format("{0} rootCauseMsg: {1} Host: {2}",
                        ex.getMessage(), rootCause.getMessage(), InetAddress.getLocalHost().getHostName()));
        simpleResult.setResult(Maps.newHashMap());
        return simpleResult;
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseBody
    public SimpleResult<?> handleInvalidParameterException(InvalidParameterException ex) throws UnknownHostException {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        SimpleResult<Map<Object, Object>> simpleResult = SimpleResult.fail(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(),
                MessageFormat.format("{0} rootCauseMsg: {1} Host: {2}",
                        ex.getMessage(), rootCause.getMessage(), InetAddress.getLocalHost().getHostName()));
        simpleResult.setResult(Maps.newHashMap());
        return simpleResult;
    }

}
