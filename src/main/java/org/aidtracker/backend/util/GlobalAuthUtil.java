package org.aidtracker.backend.util;

import org.aidtracker.backend.domain.account.Account;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @author mtage
 * @since 2020/7/28 14:15
 */
public class GlobalAuthUtil {
    public static Account authedAccount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(principal)) {
            throw new CommonSysException(AidTrackerCommonErrorCode.FORBIDDEN.getErrorCode(),
                    "未登录");
        }
        return (Account) principal;
    }
}
