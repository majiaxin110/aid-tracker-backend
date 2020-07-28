package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;

/**
 * @author mtage
 * @since 2020/7/28 12:17
 */
@Data
@ApiModel(description = "账户信息更新请求")
public class AccountUpdateRequest {
    private String name;

    @ApiModelProperty("资质描述")
    private String qualification;

    @ApiModelProperty("地区代码 <a href=\"https://lbs.qq.com/service/webService/webServiceGuiwebServiceDistrict\">行政区划代码</href>")
    private String areaAdCode;

    private Contact contact;
}
