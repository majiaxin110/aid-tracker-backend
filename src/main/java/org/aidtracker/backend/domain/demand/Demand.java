package org.aidtracker.backend.domain.demand;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

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
}
