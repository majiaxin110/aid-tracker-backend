package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.domain.demand.DemandStatusEnum;
import org.aidtracker.backend.domain.supply.SupplyProjectStatusEnum;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author mtage
 * @since 2020/7/28 14:06
 */
@Data
@ApiModel("单个需求信息 含有相关联的需求项目数量")
public class DemandWithSupplyDTO {
    private Long demandId;

    private DemandDTO demand;

    @ApiModelProperty("相关联的捐赠项目状态数量")
    private Map<SupplyProjectStatusEnum, Long> supplyProjectCountMap;

    public static DemandWithSupplyDTO fromDemand(Demand demand, Map<SupplyProjectStatusEnum, Long> projectCountMap) {
        DemandWithSupplyDTO result = new DemandWithSupplyDTO();
        result.setDemandId(demand.getDemandId());
        result.setDemand(DemandDTO.fromDemand(demand));
        result.setSupplyProjectCountMap(projectCountMap);
        return result;
    }
}
