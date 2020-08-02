package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.aidtracker.backend.domain.supply.SupplyProjectLogTypeEnum;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author mtage
 * @since 2020/8/2 11:03
 */
@Data
@ApiModel("单个捐赠项目全部进程记录")
public class SupplyProjectAllLogDTO {
    @Data
    public static class LogInfo {
        private long logId;

        private long supplyProjectId;

        private SupplyProjectLogTypeEnum logType;

        private ZonedDateTime time;

        private String info;

        private List<CosFileDTO> files;
    }

    private Long supplyProjectId;

    private List<LogInfo> logs;

}
