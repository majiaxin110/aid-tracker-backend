package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.supply.SupplyDeliverMethodEnum;
import org.aidtracker.backend.domain.supply.SupplyProject;
import org.aidtracker.backend.domain.supply.SupplyProjectStatusEnum;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author mtage
 * @since 2020/7/29 10:33
 */
@Data
public class SupplyProjectDTO {
    private Long supplyProjectId;

    @ApiModelProperty("发布者")
    private AccountDTO account;

    private Long demandId;

    @ApiModelProperty("计划捐赠数量")
    private BigDecimal amount;

    private SupplyProjectStatusEnum status;

    private SupplyDeliverMethodEnum deliverMethod;

    @ApiModelProperty("自提地址")
    private DeliverAddress address;

    private ZonedDateTime applyTime;

    private Contact contact;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("最近一条日志记录")
    private SupplyProjectLogDTO recentLog;

    public static SupplyProjectDTO fromSupplyProject(SupplyProject supplyProject, Account account) {
        SupplyProjectDTO supplyProjectDTO = new SupplyProjectDTO();
        supplyProjectDTO.setSupplyProjectId(supplyProject.getSupplyProjectId());
        supplyProjectDTO.setAccount(AccountDTO.fromAccount(account));
        supplyProjectDTO.setDemandId(supplyProject.getDemandId());
        supplyProjectDTO.setAmount(supplyProject.getAmount());
        supplyProjectDTO.setStatus(supplyProject.getStatus());
        supplyProjectDTO.setDeliverMethod(supplyProject.getDeliverMethod());
        supplyProjectDTO.setAddress(supplyProject.getAddress());
        supplyProjectDTO.setApplyTime(supplyProject.getApplyTime());
        supplyProjectDTO.setContact(supplyProject.getContact());
        supplyProjectDTO.setComment(supplyProject.getComment());
        return supplyProjectDTO;
    }

    public static SupplyProjectDTO fromSupplyProject(SupplyProject supplyProject, SupplyProjectLogDTO recentLog, Account account) {
        SupplyProjectDTO result = fromSupplyProject(supplyProject, account);
        result.setRecentLog(recentLog);
        return result;
    }
}
