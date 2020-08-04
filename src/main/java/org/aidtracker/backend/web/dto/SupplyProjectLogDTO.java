package org.aidtracker.backend.web.dto;

import lombok.Data;
import org.aidtracker.backend.domain.supply.SupplyProjectLog;
import org.aidtracker.backend.domain.supply.SupplyProjectLogTypeEnum;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author mtage
 * @since 2020/8/4 15:20
 */
@Data
public class SupplyProjectLogDTO {
    private Long logId;

    private Long supplyProjectId;

    private SupplyProjectLogTypeEnum logType;

    private ZonedDateTime time;

    private String info;

    private List<CosFileDTO> files;

    public static SupplyProjectLogDTO fromSupplyProjectLog(SupplyProjectLog supplyProjectLog, List<CosFileDTO> files) {
        SupplyProjectLogDTO supplyProjectLogDTO = new SupplyProjectLogDTO();
        supplyProjectLogDTO.setLogId(supplyProjectLog.getLogId());
        supplyProjectLogDTO.setSupplyProjectId(supplyProjectLog.getSupplyProjectId());
        supplyProjectLogDTO.setLogType(supplyProjectLog.getLogType());
        supplyProjectLogDTO.setTime(supplyProjectLog.getTime());
        supplyProjectLogDTO.setInfo(supplyProjectLog.getInfo());
        return supplyProjectLogDTO;
    }
}
