package org.aidtracker.backend.domain.supply;

import lombok.Getter;

/**
 * @author mtage
 * @since 2020/7/25 13:14
 */
public enum SupplyProjectStatusEnum {
    /**
     * 提交捐赠申请
     */
    DONATE_SUBMIT("捐赠申请"),

    GRANTEE_REPLY("受捐方回复"),

    DISPATCH("填写物流"),

    LOGISTICS_TRACKING("运输追踪"),

    DONATE_CERT("捐赠证明"),

    DONE("已完成"),

    FAILED("对接失败"),

    ;

    @Getter
    private String desc;

    SupplyProjectStatusEnum(String desc) {
        this.desc = desc;
    }
}
