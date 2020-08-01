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

    private static SupplyProjectLog of(long supplyProjectId) {
        SupplyProjectLog projectLog = new SupplyProjectLog();
        projectLog.setTime(ZonedDateTime.now());
        projectLog.setSupplyProjectId(supplyProjectId);
        return projectLog;
    }

    public static SupplyProjectLog of(SupplyProject supplyProject, SupplyProjectLogTypeEnum logType) {
        SupplyProjectLog projectLog = of(supplyProject.getSupplyProjectId());
        projectLog.setLogType(logType);
        projectLog.setInfo(logType.getDesc());
        return projectLog;
    }

    public static SupplyProjectLog of(SupplyProject supplyProject, SupplyProjectLogTypeEnum logType, List<Long> fileIds) {
        SupplyProjectLog projectLog = of(supplyProject, logType);
        projectLog.setFileIds(fileIds);
        return projectLog;
    }
}
