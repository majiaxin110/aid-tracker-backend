package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author mtage
 * @since 2020/7/25 12:48
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByWechatOpenId(String openId);

    Account findByWechatOpenIdAndRole(String wechatOpenId, AccountRoleEnum role);
}
