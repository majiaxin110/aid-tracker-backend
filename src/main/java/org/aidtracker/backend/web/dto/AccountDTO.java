package org.aidtracker.backend.web.dto;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;

/**
 * @author mtage
 * @since 2020/7/27 15:28
 */
@Data
public class AccountDTO {
    private String name;

    /**
     * 资质
     */
    private String qualification;

    /**
     * 地区代码
     * @see <a href="https://lbs.qq.com/service/webService/webServiceGuide/webServiceDistrict">行政区划代码</href>
     */
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
