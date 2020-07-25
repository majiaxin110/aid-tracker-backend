package org.aidtracker.backend.domain.supply;

import org.aidtracker.backend.domain.Contact;

import javax.persistence.*;

/**
 * @author mtage
 * @since 2020/7/25 13:34
 */
@Entity
@Table(indexes = {
        @Index(name = "idx_supplyid", columnList = "supplyProjectId")
})
public class DeliverPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deliverPeriodId;

    @Column(nullable = false)
    private long supplyProjectId;

    @Enumerated(EnumType.STRING)
    private DeliverPeriodTypeEnum type;

    /**
     * 物流单号
     */
    private String trackingNum;

    @Embedded
    private Contact contact;
}
