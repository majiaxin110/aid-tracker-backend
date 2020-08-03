package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mtage
 * @since 2020/8/3 09:11
 */
@Data
@ApiModel("捐赠项目的追踪信息 包括详细物流方式、快递信息追踪")
public class SupplyProjectTrackDTO {
    private Long supplyProjectId;

    private List<DeliverPeriodDTO> deliverPeriods;

    @ApiModelProperty("快递物流追踪 追踪有快递信息的最近环节")
    private ExpressHistoryDTO expressHistory;
}
