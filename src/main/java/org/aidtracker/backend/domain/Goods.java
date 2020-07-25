package org.aidtracker.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * 物资 如口罩/袜子...
 * Immutable Value Object
 * @author mtage
 * @since 2020/7/24 15:00
 */
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    /**
     * 物资种类/物资名称
     */
    private String goodsName;

    /**
     * 物资规格 N95/...
     */
    private String goodsSpec;

    /**
     * 制造商
     */
    private String goodsManufacturer;

    /**
     * 单位 双/枚/箱...
     */
    private String goodsUnit;
}
