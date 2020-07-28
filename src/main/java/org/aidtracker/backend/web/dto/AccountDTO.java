package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;

/**
 * @author mtage
 * @since 2020/7/27 15:28
 */
@Data
@ApiModel(description = "账户信息")
public class AccountDTO {
    private String name;

    @ApiModelProperty("资质描述")
    private String qualification;

    @ApiModelProperty("地区代码 <a href=\"https://lbs.qq.com/service/webService/webServiceGuiwebServiceDistrict\">行政区划代码</href>")
    private String areaAdCode;

    private AccountRoleEnum role;

    /**
     * 微信账号id
     */
    private String wechatOpenId;

    /**
     * 联系方式
     */
    private Contact contact;

    private String loginToken;

    public static AccountDTO fromAccount(Account account, String token) {
        AccountDTO accountDTO = fromAccount(account);
        accountDTO.setLoginToken(token);
        return accountDTO;
    }

    public static AccountDTO fromAccount(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setName(account.getName());
        accountDTO.setQualification(account.getQualification());
        accountDTO.setAreaAdCode(account.getAreaAdCode());
        accountDTO.setRole(account.getRole());
        accountDTO.setWechatOpenId(account.getWechatOpenId());
        accountDTO.setContact(account.getContact());
        return accountDTO;
    }
}
