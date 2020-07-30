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
    private SupplyProjectLogTypeEnum logType;

    private ZonedDateTime time;

    private String info;

    @Convert(converter = SimpleStringListConverter.class)
    private List<Long> fileIds;

    public static SupplyProjectLog of() {
        SupplyProjectLog projectLog = new SupplyProjectLog();
        projectLog.setTime(ZonedDateTime.now());
        return projectLog;
    }

    public static SupplyProjectLog of(SupplyProjectLogTypeEnum logType) {
        SupplyProjectLog projectLog = of();
        projectLog.setLogType(logType);
        return projectLog;
    }

    public static SupplyProjectLog of(SupplyProjectLogTypeEnum logType, List<Long> fileIds) {
        SupplyProjectLog projectLog = of(logType);
        projectLog.setFileIds(fileIds);
        return projectLog;
    }
}
