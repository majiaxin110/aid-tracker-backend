package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.demand.Demand;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author mtage
 * @since 2020/7/28 14:06
 */
@Data
@ApiModel("单个需求信息")
public class DemandDTO {
    private Long demandId;

    @ApiModelProperty("发布者id")
    private Long accountId;

    private String topic;

    @ApiModelProperty("受益方描述")
    private String audiences;

    @ApiModelProperty("需求数量")
    private BigDecimal amount;

    @ApiModelProperty("当前已经被满足的需求数量")
    private BigDecimal metAmount;

    private DeliverAddress address;

    @ApiModelProperty("自提范围信息")
    private String selfTakeInfo;

    @ApiModelProperty("发布时间")
    private ZonedDateTime publishTime;

    private Contact contact;

    @ApiModelProperty("备注")
    private String comment;

    public static DemandDTO fromDemand(Demand demand) {
        DemandDTO demandDTO = new DemandDTO();
        demandDTO.setDemandId(demand.getDemandId());
        demandDTO.setAccountId(demand.getAccountId());
        demandDTO.setTopic(demand.getTopic());
        demandDTO.setAudiences(demand.getAudiences());
        demandDTO.setAmount(demand.getAmount());
        demandDTO.setMetAmount(demand.getMetAmount());
        demandDTO.setAddress(demand.getAddress());
        demandDTO.setSelfTakeInfo(demand.getSelfTakeInfo());
        demandDTO.setPublishTime(demand.getPublishTime());
        demandDTO.setContact(demand.getContact());
        demandDTO.setComment(demand.getComment());
        return demandDTO;
    }
}
