package org.aidtracker.backend.domain.supply;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static org.aidtracker.backend.domain.supply.SupplyProjectStatusEnum.*;

/**
 * @author mtage
 * @since 2020/7/24 15:14
 */
@Entity
@Data
@Table(indexes = {
        @Index(name = "idx_accountid", columnList = "accountId"),
        @Index(name = "idx_demandid", columnList = "demandId")
})
public class SupplyProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long supplyProjectId;

    /**
     * 发布者Id
     */
    @Column(nullable = false)
    private long accountId;

    /**
     * 关联的需求id
     */
    @Column(nullable = false)
    private long demandId;

    @Embedded
    private Goods goods;

    @Column(nullable = false, columnDefinition = "decimal(19, 2) default 0")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private SupplyProjectStatusEnum status;

    @Enumerated(EnumType.STRING)
    private SupplyDeliverMethodEnum deliverMethod;

    /**
     * 自提地址？
     */
    private DeliverAddress address;

    private ZonedDateTime applyTime;

    /**
     * 联系方式
     */
    @Embedded
    private Contact contact;

    @Column(columnDefinition = "TEXT")
    private String comment;

    /**
     * 受捐方同意捐赠
     * 状态转变为 GRANTEE_REPLY
     * @param grantee
     * @return
     */
    public SupplyProjectLog granteeAgreed(Account grantee) {
        verifyStatus(this.status, GRANTEE_REPLY);
        this.status = GRANTEE_REPLY;
        return SupplyProjectLog.of(this, SupplyProjectLogTypeEnum.GRANTEE_AGREE);
    }

    /**
     * 捐赠方进行了发货
     * 状态转变为 LOGISTICS_TRACKING
     * @param deliverPeriodList
     * @return
     */
    public SupplyProjectLog dispatch(List<DeliverPeriod> deliverPeriodList) {
        if (CollectionUtils.isEmpty(deliverPeriodList)) {
            throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(),
                    "发货物流信息不能为空");
        }
        verifyStatus(this.status, LOGISTICS_TRACKING);
        this.status = LOGISTICS_TRACKING;
        return SupplyProjectLog.of(this, SupplyProjectLogTypeEnum.DISPATCH);
    }

    /**
     * 受捐方确认了收货
     * 状态转变为 DONATE_CERT 等待受捐方开具证明
     * @return
     */
    public SupplyProjectLog granteeConfirm() {
        verifyStatus(this.status, DONATE_CERT);
        this.status = DONATE_CERT;
        return SupplyProjectLog.of(this, SupplyProjectLogTypeEnum.GRANTEE_CONFIRM);
    }

    /**
     * 受捐方开具上传了证明
     * 状态转变为 DONE 捐赠完成
     * @return
     */
    public SupplyProjectLog donateCert(List<Long> fileIds) {
        verifyStatus(this.status, DONE);
        this.status = DONE;
        return SupplyProjectLog.of(this, SupplyProjectLogTypeEnum.DONATE_CERT, fileIds);
    }

}
