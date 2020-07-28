package org.aidtracker.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aidtracker.backend.dao.AccountRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.aidtracker.backend.web.service.WechatAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 测试账户环境
 * @author mtage
 * @since 2020/7/28 11:10
 */
@SpringBootTest
public class AccountEnvBaseTest extends BaseTest {
    protected final static String testAccountName = "Eyjafjalla";
    protected final static String testOpenId = "test-70G52s8v3l-bXiPxTGWP-jY7";
    protected final static AccountRoleEnum testRole = AccountRoleEnum.DONATOR;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    WechatAuthService wechatAuthService;

    @BeforeEach
    public void setUpTestAccount() {
        Account account = new Account();
        account.setName(testAccountName);
        account.setWechatOpenId(testOpenId);
        account.setRole(testRole);

        Account existAccount = accountRepository.findByWechatOpenIdAndRole(testOpenId, testRole);
        if (Objects.isNull(existAccount)) {
            accountRepository.save(account);
        }

        when(wechatAuthService.auth(anyString())).thenReturn(testOpenId);
    }

    @AfterEach
    public void clearTestAccount() {
        Account existAccount = accountRepository.findByWechatOpenIdAndRole(testOpenId, testRole);
        if (Objects.nonNull(existAccount)) {
            accountRepository.delete(existAccount);
        }
    }
}
