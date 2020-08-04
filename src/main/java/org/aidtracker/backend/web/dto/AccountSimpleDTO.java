package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;

/**
 * 仅包含基本账户信息
 * @author mtage
 * @since 2020/8/4 21:27
 */
@Data
public class AccountSimpleDTO {
    private Long accountId;

    private String name;

    @ApiModelProperty("资质描述")
    private String qualification;

    private String areaAdCode;

    private AccountRoleEnum role;

    private Contact contact;

    public static AccountSimpleDTO fromAccount(Account account) {
        AccountSimpleDTO accountSimpleDTO = new AccountSimpleDTO();
        accountSimpleDTO.setAccountId(account.getAccountId());
        accountSimpleDTO.setName(account.getName());
        accountSimpleDTO.setQualification(account.getQualification());
        accountSimpleDTO.setAreaAdCode(account.getAreaAdCode());
        accountSimpleDTO.setRole(account.getRole());
        accountSimpleDTO.setContact(account.getContact());
        return accountSimpleDTO;
    }

}
