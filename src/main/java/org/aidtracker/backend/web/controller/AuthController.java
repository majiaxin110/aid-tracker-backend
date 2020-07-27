package org.aidtracker.backend.web.controller;

import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.AccountDTO;
import org.aidtracker.backend.web.dto.AccountRegisterRequest;
import org.aidtracker.backend.web.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author mtage
 * @since 2020/7/27 11:13
 */
@RestController
public class AuthController {
    @Autowired
    private AccountService accountService;

    public static final String NEW_USER = "NEW_USER";

    @PostMapping("/auth/login")
    public SimpleResult<?> tryLogin(@RequestParam String jsCode) {
        AccountDTO result = accountService.tryLogin(jsCode);
        if (Objects.isNull(result)) {
            return SimpleResult.success(NEW_USER);
        }
        return SimpleResult.success(result);
    }

    @PostMapping("/auth/register")
    public SimpleResult<AccountDTO> tryRegister(@RequestBody AccountRegisterRequest request) {
        return SimpleResult.success(accountService.tryRegister(request));
    }

}
