package org.aidtracker.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * 抽象的寄送地址 目前详细地址均由用户手动填写
 * Immutable Value Object
 * @author mtage
 * @since 2020/7/24 15:04
 */
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliverAddress {
    private String deliverZipCode;
    private String deliverAddressInfo;
    @Column(columnDefinition = "decimal(9, 6)")
    private BigDecimal deliverAddressLon;
    @Column(columnDefinition = "decimal(9, 6)")
    private BigDecimal deliverAddressLat;
}
