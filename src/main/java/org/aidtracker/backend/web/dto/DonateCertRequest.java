package org.aidtracker.backend.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mtage
 * @since 2020/8/1 14:15
 */
@Data
public class DonateCertRequest {
    @NotNull
    private Long supplyProjectId;

    @ApiModelProperty("捐献证明文件id列表")
    private List<Long> fileIds;
}
