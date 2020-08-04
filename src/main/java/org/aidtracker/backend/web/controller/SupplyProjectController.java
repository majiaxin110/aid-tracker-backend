package org.aidtracker.backend.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aidtracker.backend.domain.supply.SupplyProjectStatusEnum;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.util.GlobalAuthUtil;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.*;
import org.aidtracker.backend.web.service.DemandService;
import org.aidtracker.backend.web.service.SupplyProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author mtage
 * @since 2020/7/29 10:57
 */
@RestController
public class SupplyProjectController {
    final SupplyProjectService supplyProjectService;
    final DemandService demandService;

    @Autowired
    public SupplyProjectController(SupplyProjectService supplyProjectService, DemandService demandService) {
        this.supplyProjectService = supplyProjectService;
        this.demandService = demandService;
    }

    @ApiOperation("新建 捐赠项目")
    @PostMapping("/supply-project")
    @PreAuthorize("hasAnyAuthority('DONATOR')")
    public SimpleResult<SupplyProjectDTO> create(@RequestBody @ApiParam("SupplyProjectCreateRequest") SupplyProjectCreateRequest request) {
        return SimpleResult.success(supplyProjectService.create(request, GlobalAuthUtil.authedAccount()));
    }

    @ApiOperation("更新 捐赠项目")
    @PutMapping("/supply-project")
    @PreAuthorize("hasAnyAuthority('DONATOR')")
    public SimpleResult<SupplyProjectDTO> update(@RequestBody @ApiParam("SupplyProjectUpdateRequest") SupplyProjectUpdateRequest request) {
        return SimpleResult.success(supplyProjectService.update(request, GlobalAuthUtil.authedAccount()));
    }

    @ApiOperation("查询 捐赠项目")
    @GetMapping("/supply-project")
    public SimpleResult<SupplyProjectDTO> getById(@RequestParam Long supplyProjectId) {
        return SimpleResult.success(supplyProjectService.getById(supplyProjectId, GlobalAuthUtil.authedAccount()));
    }

    @ApiOperation("查询 捐赠项目列表")
    @GetMapping("/supply-project/list")
    public SimpleResult<Map<SupplyProjectStatusEnum, List<SupplyProjectDTO>>> getList(@RequestParam SupplyProjectListQueryTypeEnum type,
                                                                                      @RequestParam(required = false) Long demandId) {
        if (type == SupplyProjectListQueryTypeEnum.SELF) {
            return SimpleResult.success(supplyProjectService.allSupplyProjectByAccount(GlobalAuthUtil.authedAccount()));
        } else if (type == SupplyProjectListQueryTypeEnum.DEMAND) {
            return SimpleResult.success(supplyProjectService.allSupplyProjectByDemand(demandService.findByIdAccount(demandId, GlobalAuthUtil.authedAccount())));
        }
        throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), "非法的查询类型");
    }

    @ApiOperation("受捐方同意捐赠一个项目")
    @PostMapping("/supply-project/grantee-agree")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<String> granteeAgree(@RequestBody Long supplyProjectId) {
        supplyProjectService.granteeAgree(supplyProjectId, GlobalAuthUtil.authedAccount());
        return SimpleResult.ok();
    }

    @ApiOperation("捐赠方发货")
    @PostMapping("/supply-project/dispatch")
    @PreAuthorize("hasAnyAuthority('DONATOR')")
    public SimpleResult<String> donatorDispatch(@RequestBody DispatchRequest request) {
        supplyProjectService.donatorDispatch(request, GlobalAuthUtil.authedAccount());
        return SimpleResult.ok();
    }

    @ApiOperation("受捐方收货")
    @PostMapping("/supply-project/grantee-received")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<String> granteeReceived(@RequestBody GranteeReceivedRequest request) {
        supplyProjectService.granteeReceived(request, GlobalAuthUtil.authedAccount());
        return SimpleResult.ok();
    }

    @ApiOperation("受捐方提交捐献证明")
    @PostMapping("/supply-project/donate-cert")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<String> granteeSubmitCert(@RequestBody DonateCertRequest request) {
        supplyProjectService.granteeSubmitCert(request, GlobalAuthUtil.authedAccount());
        return SimpleResult.ok();
    }

}
