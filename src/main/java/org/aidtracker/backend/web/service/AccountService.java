package org.aidtracker.backend.web.service;

import org.aidtracker.backend.dao.AccountRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.util.JwtTokenUtil;
import org.aidtracker.backend.web.dto.AccountDTO;
import org.aidtracker.backend.web.dto.AccountRegisterRequest;
import org.aidtracker.backend.web.dto.AccountUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author mtage
 * @since 2020/7/27 13:58
 */
@Service
public class AccountService {
    private final WechatAuthService wechatAuthService;

    private final AccountRepository accountRepository;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AccountService(WechatAuthService wechatAuthService, AccountRepository accountRepository, JwtTokenUtil jwtTokenUtil) {
        this.wechatAuthService = wechatAuthService;
        this.accountRepository = accountRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 尝试通过临时code登录
     * @param jsCode
     * @return
     */
    public AccountDTO tryLogin(String jsCode, AccountRoleEnum role) {
        String openId = wechatAuthService.auth(jsCode);
        // 允许不指定角色类型的登录
        if (Objects.isNull(role)) {
            List<Account> accountList = accountRepository.findAllByWechatOpenId(openId);
            if (accountList.size() > 0) {
                return AccountDTO.fromAccount(accountList.get(0), jwtTokenUtil.generateToken(accountList.get(0)));
            }
        } else {
            Account account = accountRepository.findByWechatOpenIdAndRole(openId, role);
            if (Objects.nonNull(account)) {
                return AccountDTO.fromAccount(account, jwtTokenUtil.generateToken(account));
            }
        }
        return null;
    }

    /**
     * 尝试注册
     * @param request
     * @throws IllegalArgumentException 角色类型无效时
     * @return
     */
    public AccountDTO tryRegister(AccountRegisterRequest request) {
        String wechatOpenId = wechatAuthService.auth(request.getJsCode());

        Account account = new Account();
        account.setName(request.getName());
        account.setQualification(request.getQualification());
        account.setAreaAdCode(request.getAreaAdCode());
        account.setRole(request.getRole());
        account.setWechatOpenId(wechatOpenId);
        account.setContact(request.getContact());

        accountRepository.save(account);

        return AccountDTO.fromAccount(account, jwtTokenUtil.generateToken(account));
    }

    public AccountDTO update(AccountUpdateRequest request) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        account.setName(request.getName());
        account.setContact(request.getContact());
        account.setAreaAdCode(request.getAreaAdCode());
        account.setQualification(request.getQualification());

        accountRepository.save(account);

        return AccountDTO.fromAccount(account);
    }

    public Account getById(long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new CommonSysException(AidTrackerCommonErrorCode.NOT_FOUND.getErrorCode(),
                "不存在的账户 " + accountId));
    }

}
