package org.aidtracker.backend.domain.supply;

import lombok.Data;
import org.aidtracker.backend.dao.util.SimpleStringListConverter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author mtage
 * @since 2020/7/25 13:21
 */
@Entity
@Table(indexes = {
        @Index(name = "idx_supplyid", columnList = "supplyProjectId")
})
@Data
public class SupplyProjectLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;

    @Column(nullable = false)
    private long supplyProjectId;

    @Enumerated(EnumType.STRING)
    private SupplyProjectLogTypeEnum type;

    private ZonedDateTime time;

    private String info;

    @Convert(converter = SimpleStringListConverter.class)
    private List<String> fileIds;
}
