package org.aidtracker.backend.web.dto;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;

/**
 * 用户信息更新请求
 * @author mtage
 * @since 2020/7/28 12:17
 */
@Data
public class AccountUpdateRequest {
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

    private Contact contact;
}
