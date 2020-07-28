package org.aidtracker.backend.web.controller;

import io.swagger.annotations.*;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.AccountDTO;
import org.aidtracker.backend.web.dto.AccountRegisterRequest;
import org.aidtracker.backend.web.dto.AccountUpdateRequest;
import org.aidtracker.backend.web.service.AccountService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author mtage
 * @since 2020/7/27 11:13
 */
@RestController
@Api("认证登录")
public class AuthController {
    @Autowired
    private AccountService accountService;

    public static final String NEW_USER = "NEW_USER";

    @PostMapping("/auth/login")
    @ApiOperation(value = "登录",
            notes = "登录注册流程参考上方文档及微信小程序登录流程 https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "新用户初次登录返回\"NEW_USER\"")
    })
    public SimpleResult<?> tryLogin(@RequestParam @ApiParam(value = "调用wx.login()产生的临时登录凭证Id", required = true) String jsCode,
                                    @RequestParam(required = false) @ApiParam(value = "指定角色类型", required = false) AccountRoleEnum role) {
        AccountDTO result = accountService.tryLogin(jsCode, role);
        if (Objects.isNull(result)) {
            return SimpleResult.success(NEW_USER);
        }
        return SimpleResult.success(result);
    }

    @PostMapping("/auth/register")
    @ApiOperation(value = "注册")
    public SimpleResult<AccountDTO> tryRegister(@RequestBody AccountRegisterRequest request) {
        return SimpleResult.success(accountService.tryRegister(request));
    }

    @PutMapping("/account")
    @ApiOperation(value = "用户信息更新")
    public SimpleResult<AccountDTO> update(@RequestBody AccountUpdateRequest request) {
        return SimpleResult.success(accountService.update(request));
    }

}
