package org.aidtracker.backend.web.controller;

import io.swagger.annotations.ApiOperation;
import org.aidtracker.backend.util.GlobalAuthUtil;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.SupplyProjectAllLogDTO;
import org.aidtracker.backend.web.dto.SupplyProjectTrackDTO;
import org.aidtracker.backend.web.service.SupplyProjectTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mtage
 * @since 2020/8/3 09:24
 */
@RestController
public class SupplyProjectTrackController {
    @Autowired
    private SupplyProjectTrackService supplyProjectTrackService;

    @GetMapping("/supply-project/log")
    @ApiOperation("单个捐赠项目的全部进程记录")
    public SimpleResult<SupplyProjectAllLogDTO> getAllLog(@RequestParam long supplyProjectId) {
        return SimpleResult.success(supplyProjectTrackService.allLogById(supplyProjectId, GlobalAuthUtil.authedAccount()));
    }

    @GetMapping("/supply-project/deliver-period")
    @ApiOperation("单个捐赠项目的运输阶段和物流追踪")
    public SimpleResult<SupplyProjectTrackDTO> trackExpress(@RequestParam long supplyProjectId) {
        return SimpleResult.success(supplyProjectTrackService.trackExpressById(supplyProjectId, GlobalAuthUtil.authedAccount()));
    }
}
