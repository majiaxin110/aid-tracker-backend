package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;

import java.math.BigDecimal;

/**
 * @author mtage
 * @since 2020/7/28 13:43
 */
@Data
public class DemandCreateRequest {
    private String topic;

    @ApiModelProperty("受益方描述 援助受众")
    private String audiences;

    private Goods goods;

    @ApiModelProperty("需求数量")
    private BigDecimal amount;

    @ApiModelProperty("寄送地址")
    private DeliverAddress address;

    @ApiModelProperty("自提范围信息")
    private String selfTakeInfo;

    @ApiModelProperty("联系方式")
    private Contact contact;

    @ApiModelProperty("备注")
    private String comment;
}
