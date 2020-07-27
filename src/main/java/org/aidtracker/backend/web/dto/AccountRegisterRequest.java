package org.aidtracker.backend.web.dto;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.account.AccountRoleEnum;

/**
 * 新用户注册请求
 * @author mtage
 * @since 2020/7/27 10:51
 */
@Data
public class AccountRegisterRequest {
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

    private Contact contact;

    private String jsCode;
}
