package org.aidtracker.backend.util;

import org.aidtracker.backend.domain.account.Account;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author mtage
 * @since 2020/7/28 14:15
 */
public class GlobalAuthUtil {
    public static Account authedAccount() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
