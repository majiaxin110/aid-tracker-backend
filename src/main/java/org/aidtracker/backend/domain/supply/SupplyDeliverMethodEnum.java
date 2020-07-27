package org.aidtracker.backend.domain.supply;

/**
 * 捐赠预计交付方式
 * @author mtage
 * @since 2020/7/25 13:15
 */
public enum SupplyDeliverMethodEnum {
    /**
     * 捐赠方负责运输
     */
    DONATOR,

    /**
     * 受赠方自取
     */
    GRANTEE_SELF_TAKE
}
