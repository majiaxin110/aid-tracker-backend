package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.supply.DeliverPeriod;
import org.aidtracker.backend.domain.supply.DeliverPeriodTypeEnum;

/**
 * @author mtage
 * @since 2020/8/3 09:12
 */
@Data
public class DeliverPeriodDTO {
    private Long deliverPeriodId;

    private Long supplyProjectId;

    private DeliverPeriodTypeEnum periodType;

    @ApiModelProperty("物流单号")
    private String trackingNum;

    private Contact contact;

    public static DeliverPeriodDTO fromDeliverPeriod(DeliverPeriod deliverPeriod) {
        DeliverPeriodDTO result = new DeliverPeriodDTO();
        result.setContact(deliverPeriod.getContact());
        result.setDeliverPeriodId(deliverPeriod.getDeliverPeriodId());
        result.setPeriodType(deliverPeriod.getPeriodType());
        result.setTrackingNum(deliverPeriod.getTrackingNum());
        result.setSupplyProjectId(deliverPeriod.getSupplyProjectId());
        return result;
    }
}
