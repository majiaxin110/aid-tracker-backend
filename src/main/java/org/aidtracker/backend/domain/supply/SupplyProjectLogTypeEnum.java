package org.aidtracker.backend.domain.supply;

import lombok.Getter;

/**
 * @author mtage
 * @since 2020/7/25 13:22
 */
public enum SupplyProjectLogTypeEnum {

    /**
     * 提交捐赠申请
     */
    DONATE_SUBMIT("提交捐赠申请"),

    GRANTEE_AGREE("受捐方同意捐赠"),

    DISPATCH("确认发货"),

    LOGISTICS_TRACKING("运输追踪"),

    GRANTEE_RECEIVED("受捐方确认收货"),

    DONATE_CERT("受捐方开具捐献证明"),

    FAILURE("对接失败"),

    OTHERS("其他")
    ;

    @Getter
    private String desc;

    SupplyProjectLogTypeEnum(String desc) {
        this.desc = desc;
    }
}
