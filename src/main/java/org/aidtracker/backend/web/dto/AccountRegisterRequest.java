package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.account.AccountRoleEnum;

/**
 * 新用户注册请求
 * @author mtage
 * @since 2020/7/27 10:51
 */
@Data
@ApiModel(description = "账户注册请求")
public class AccountRegisterRequest {
    private String name;

    @ApiModelProperty("资质描述")
    private String qualification;

    @ApiModelProperty("地区代码 <a href=\"https://lbs.qq.com/service/webService/webServiceGuiwebServiceDistrict\">行政区划代码</href>")
    private String areaAdCode;

    @ApiModelProperty("账户类型 捐赠方/受捐方")
    private AccountRoleEnum role;

    private Contact contact;

    @ApiModelProperty("调用wx.login()生成的临时登录凭证")
    private String jsCode;
}
