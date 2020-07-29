package org.aidtracker.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.dao.AccountRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.aidtracker.backend.web.service.WechatAuthService;
import org.junit.jupiter.api.AfterEach;
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
    protected final static String testAccountName = "Eyjafjalla";
    protected final static String testOpenId = "test-70G52s8v3l-bXiPxTGWP-jY7";
    protected final static AccountRoleEnum testRole = AccountRoleEnum.DONATOR;
    protected static Account testAccount;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    WechatAuthService wechatAuthService;

    static {
        testAccount = new Account();
        testAccount.setName(testAccountName);
        testAccount.setWechatOpenId(testOpenId);
        testAccount.setRole(testRole);
    }

    @BeforeEach
    public void setUpTestAccount() {
        Account existAccount = accountRepository.getByWechatOpenIdAndRole(testOpenId, testRole);
        if (Objects.isNull(existAccount)) {
            testAccount = accountRepository.save(testAccount);
        }

        when(wechatAuthService.auth(anyString())).thenReturn(testOpenId);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testAccount, null,
                        List.of(new SimpleGrantedAuthority(testRole.name())));
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
}
