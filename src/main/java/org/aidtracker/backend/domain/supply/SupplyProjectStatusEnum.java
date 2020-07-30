package org.aidtracker.backend.domain.supply;

import lombok.Getter;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

    /**
     * 验证转换状态目标是否合法
     * @param goalStatus
     */
    public static void verifyStatus(SupplyProjectStatusEnum currentStatus, SupplyProjectStatusEnum goalStatus) {
        List<SupplyProjectStatusEnum> allStatus = Arrays.asList(SupplyProjectStatusEnum.values());
        for (int i = 0; i < allStatus.size(); i++) {
            if (allStatus.get(i) == goalStatus) {
                if (i <= 0 || allStatus.get(i - 1) != currentStatus) {
                    throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(),
                            allStatus.get(i - 1).desc + " 才能进行 " + goalStatus.desc);
                }
            }
        }
    }
}
