package org.aidtracker.backend.domain.supply;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

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
}
