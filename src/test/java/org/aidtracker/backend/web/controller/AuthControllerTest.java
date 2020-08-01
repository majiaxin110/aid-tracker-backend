package org.aidtracker.backend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.AccountEnvBaseTest;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.aidtracker.backend.util.SimpleResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author mtage
 * @since 2020/7/28 11:07
 */
@SpringBootTest
@Slf4j
class AuthControllerTest extends AccountEnvBaseTest {
    @Autowired
    AuthController authController;

    @Test
    void tryLogin() throws JsonProcessingException {
        SimpleResult<?> result = authController.tryLogin("anyCode", null);
        log.warn(objectMapper.writeValueAsString(result));

        result = authController.tryLogin("anyCode", AccountRoleEnum.DONATOR);
        assertNotNull(result.getResult());
        log.warn(objectMapper.writeValueAsString(result));

        result = authController.tryLogin("anyCode", AccountRoleEnum.GRANTEE);
        assertEquals(result.getResult(), AuthController.NEW_USER);
        printResult(result);
    }

    @Test
    void tryRegister() {
    }
}