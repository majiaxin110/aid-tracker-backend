package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author mtage
 * @since 2020/8/2 11:03
 */
@Data
@ApiModel("单个捐赠项目全部进程记录")
public class SupplyProjectAllLogDTO {
    private Long supplyProjectId;

    private List<SupplyProjectLogDTO> logs;
}
