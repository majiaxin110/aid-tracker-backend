package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author mtage
 * @since 2020/8/2 14:26
 */
@ApiModel("快递物流信息")
@Data
public class ExpressHistoryDTO {
    @Data
    public static class HistoryInfo {
        private ZonedDateTime time;
        private String statusDesc;
    }

    @ApiModelProperty("物流单号")
    private String trackingNum;

    @ApiModelProperty("快递公司logo")
    private String logUrl;

    private List<HistoryInfo> list;

    @ApiModelProperty("是否有有效的物流信息")
    private Boolean valid;
}
