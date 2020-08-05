package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.supply.DeliverPeriodTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mtage
 * @since 2020/8/1 11:59
 */
@Data
public class DispatchRequest {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeliverPeriodInfo {
        private DeliverPeriodTypeEnum periodType;

        @ApiModelProperty("物流单号")
        private String trackingNum;

        private Contact contact;
    }

    @ApiModelProperty("物流阶段")
    List<DeliverPeriodInfo> deliverPeriodList;

    @NotNull
    Long supplyProjectId;
}
