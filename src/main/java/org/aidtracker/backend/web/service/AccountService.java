package org.aidtracker.backend.web.service;

import org.aidtracker.backend.dao.AccountRepository;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.aidtracker.backend.util.JwtTokenUtil;
import org.aidtracker.backend.web.dto.AccountDTO;
import org.aidtracker.backend.web.dto.AccountRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

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
    public AccountDTO tryLogin(String jsCode) {
        List<Account> accountList = accountRepository.findAllByWechatOpenId(wechatAuthService.auth(jsCode));
        if (accountList.size() > 0) {
            return AccountDTO.fromAccount(accountList.get(0), jwtTokenUtil.generateToken(accountList.get(0)));
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

}
