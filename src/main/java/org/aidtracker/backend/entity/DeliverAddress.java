package org.aidtracker.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

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
public class DeliverAddress {
    private String deliverZipCode;
    private String deliverAddressInfo;
}
