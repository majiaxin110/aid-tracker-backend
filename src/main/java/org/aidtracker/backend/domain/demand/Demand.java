package org.aidtracker.backend.domain.demand;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.supply.SupplyProject;
import org.aidtracker.backend.domain.supply.SupplyProjectStatusEnum;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * 受赠者发布的需求
 * @author mtage
 * @since 2020/7/24 15:13
 */
@Entity
@Data
@Table(name = "demand", indexes = {
        @Index(name="idx_accountid", columnList = "accountId")
})
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long demandId;

    /**
     * 发布者id
     */
    @Column(nullable = false)
    private long accountId;

    private String topic;

    /**
     * 受益方描述
     */
    private String audiences;

    @Embedded
    private Goods goods;

    @Enumerated(EnumType.STRING)
    private DemandStatusEnum status;

    /**
     * 需求数量
     */
    @Column(nullable = false, columnDefinition = "decimal(19, 2) default 0")
    private BigDecimal amount;

    /**
     * 当前已经被满足的需求数量
     */
    @Column(nullable = false, columnDefinition = "decimal(19, 2) default 0")
    private BigDecimal metAmount;

    /**
     * 寄送地址
     */
    @Embedded
    private DeliverAddress address;

    /**
     * 自提范围信息
     */
    private String selfTakeInfo;

    /**
     * 发布时间
     */
    private ZonedDateTime publishTime;

    /**
     * 联系方式
     */
    @Embedded
    private Contact contact;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    private String comment;

    /**
     * 该需求可以减去一个捐赠项目的数量
     * 若减去该项目后需求完全满足，本需求状态将被设置为已完成
     * @param supplyProject
     */
    public void confirm(SupplyProject supplyProject) {
        if (Objects.isNull(supplyProject) || supplyProject.getDemandId() != this.demandId) {
            throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(),
                    "捐赠项目无效，不是该需求对应的捐赠项目");
        }
        if (supplyProject.getStatus() != SupplyProjectStatusEnum.GRANTEE_REPLY) {
            throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(),
                    "捐赠项目未处于受捐方同意状态");
        }
        this.metAmount = this.metAmount.add(supplyProject.getAmount());
        if (this.amount.compareTo(this.metAmount) == 0) {
            this.setStatus(DemandStatusEnum.DONE);
        }
    }

    public void close(Account publisher) {
        if (Objects.isNull(publisher) || publisher.getAccountId() != this.accountId) {
            throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(),
                    "非法的用户操作");
        }
        this.setStatus(DemandStatusEnum.CLOSED);
    }
}
