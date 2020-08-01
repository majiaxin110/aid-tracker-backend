package org.aidtracker.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.dao.AccountRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.service.WechatAuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 测试账户环境
 * @author mtage
 * @since 2020/7/28 11:10
 */
@SpringBootTest
@Slf4j
public class AccountEnvBaseTest extends BaseTest {
    protected final static String testDonatorAccountName = "Eyjafjalla";
    protected final static String testGranteeAccountName = "黑泽朋世";
    protected final static String testDonatorOpenId = "test-donator52s8v3l-bXiPxTGWP-jY7";
    protected final static String testGranteeOpenId = "test-grantee42s8v3l-bXiPxTGWP-jY6";
    protected static Account testDonatorAccount;
    protected static Account testGranteeAccount;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    WechatAuthService wechatAuthService;

    static {
        testDonatorAccount = new Account();
        testDonatorAccount.setName(testDonatorAccountName);
        testDonatorAccount.setWechatOpenId(testDonatorOpenId);
        testDonatorAccount.setRole(AccountRoleEnum.DONATOR);

        testGranteeAccount = new Account();
        testGranteeAccount.setName(testGranteeAccountName);
        testGranteeAccount.setWechatOpenId(testGranteeOpenId);
        testGranteeAccount.setRole(AccountRoleEnum.GRANTEE);
    }

    @BeforeEach
    public void setUpTestAccount() {
        testDonatorAccount = saveTestAccount(testDonatorAccount);
        testGranteeAccount = saveTestAccount(testGranteeAccount);

        when(wechatAuthService.auth(anyString())).thenReturn(testDonatorOpenId);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testDonatorAccount, null,
                        List.of(new SimpleGrantedAuthority(AccountRoleEnum.DONATOR.name())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setUpGranteeEnv() {
        when(wechatAuthService.auth(anyString())).thenReturn(testGranteeOpenId);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testGranteeAccount, null,
                        List.of(new SimpleGrantedAuthority(AccountRoleEnum.GRANTEE.name())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setUpDonatorEnv() {
        when(wechatAuthService.auth(anyString())).thenReturn(testDonatorOpenId);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testDonatorAccount, null,
                        List.of(new SimpleGrantedAuthority(AccountRoleEnum.DONATOR.name())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    @AfterEach
//    public void clearTestAccount() {
//        Account existAccount = accountRepository.getByWechatOpenIdAndRole(testOpenId, testRole);
//        if (Objects.nonNull(existAccount)) {
//            accountRepository.delete(existAccount);
//        }
//    }

    public void printResult(Object object) throws JsonProcessingException {
        log.warn(objectMapper.writeValueAsString(object));
    }

    public void checkAndPrint(SimpleResult<?> result) throws JsonProcessingException {
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getSuccess());

        printResult(result);
    }

    private Account saveTestAccount(Account account) {
        Account existAccount = accountRepository.findByWechatOpenIdAndRole(account.getWechatOpenId(), account.getRole());
        if (Objects.isNull(existAccount)) {
            return accountRepository.save(account);
        }
        return existAccount;
    }
}
