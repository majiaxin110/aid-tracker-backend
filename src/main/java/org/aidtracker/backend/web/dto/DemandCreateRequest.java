package org.aidtracker.backend.web.dto;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;

import java.math.BigDecimal;

/**
 * @author mtage
 * @since 2020/7/28 13:43
 */
@Data
public class DemandCreateRequest {
    private String topic;

    /**
     * 受益方描述 援助受众
     */
    private String audiences;

    /**
     * 需求数量
     */
    private BigDecimal amount;

    /**
     * 寄送地址
     */
    private DeliverAddress address;

    /**
     * 自提范围信息
     */
    private String selfTakeInfo;

    /**
     * 联系方式
     */
    private Contact contact;

    /**
     * 备注
     */
    private String comment;
}
