package org.aidtracker.backend.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aidtracker.backend.util.GlobalAuthUtil;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.SupplyProjectCreateRequest;
import org.aidtracker.backend.web.dto.SupplyProjectDTO;
import org.aidtracker.backend.web.dto.SupplyProjectUpdateRequest;
import org.aidtracker.backend.web.service.SupplyProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mtage
 * @since 2020/7/29 10:57
 */
@RestController
public class SupplyProjectController {
    final SupplyProjectService supplyProjectService;

    @Autowired
    public SupplyProjectController(SupplyProjectService supplyProjectService) {
        this.supplyProjectService = supplyProjectService;
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
}
