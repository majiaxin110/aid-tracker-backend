package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mtage
 * @since 2020/8/1 14:14
 */
@Data
public class GranteeReceivedRequest {
    private Long supplyProjectId;

    @ApiModelProperty("收货证明文件id列表")
    private List<Long> fileIds;
}
