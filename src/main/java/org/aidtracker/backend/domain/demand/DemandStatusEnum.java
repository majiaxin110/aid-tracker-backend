package org.aidtracker.backend.domain.demand;

import lombok.Getter;

/**
 * @author mtage
 * @since 2020/7/30 15:19
 */
public enum DemandStatusEnum {
    /**
     * 初始状态
     */
    DEMAND_SUBMIT("需求发布"),

    DONE("需求完成"),

    PAUSE("暂停中"),

    CLOSED("需求关闭")
    ;

    @Getter
    private String desc;

    DemandStatusEnum(String desc) {
        this.desc = desc;
    }

}
