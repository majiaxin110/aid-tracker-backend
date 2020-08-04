package org.aidtracker.backend.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aidtracker.backend.domain.demand.DemandStatusEnum;
import org.aidtracker.backend.util.GlobalAuthUtil;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.*;
import org.aidtracker.backend.web.service.DemandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author mtage
 * @since 2020/7/27 14:50
 */
@RestController
public class DemandController {
    final DemandService demandService;

    public DemandController(DemandService demandService) {
        this.demandService = demandService;
    }

    @ApiOperation("开放接口 全部需求列表")
    @GetMapping("/public/demand/list")
    public SimpleResult<Page<DemandDTO>> getAllDemand(@RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int size,
                                                      @RequestParam(required = false, defaultValue = "DEFAULT") PublicDemandListQueryTypeEnum type
                                                      ) {
        return SimpleResult.success(demandService.allDemand(page, size, type));
    }

    @ApiOperation("需求列表查询")
    @GetMapping("/demand/list")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<Map<DemandStatusEnum, List<DemandWithSupplyCountDTO>>> getDemandList(@RequestParam(defaultValue = "SELF") DemandListQueryTypeEnum type) {
        return SimpleResult.success(demandService.allDemandByAccount(GlobalAuthUtil.authedAccount()));
    }


    @ApiOperation("新建需求")
    @PostMapping("/demand")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<DemandDTO> create(@RequestBody @ApiParam("DemandCreateRequest") DemandCreateRequest request) {
        return SimpleResult.success(demandService.create(request, GlobalAuthUtil.authedAccount()));
    }

    @ApiOperation("更新需求")
    @PutMapping("/demand")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<DemandDTO> update(@RequestBody @ApiParam("DemandUpdateRequest") DemandUpdateRequest request) {
        return SimpleResult.success(demandService.update(request, GlobalAuthUtil.authedAccount()));
    }

    @ApiOperation("关闭需求")
    @DeleteMapping("/demand")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<String> delete(@RequestParam Long demandId) {
        demandService.close(demandId, GlobalAuthUtil.authedAccount());
        return SimpleResult.ok();
    }

}
