package org.aidtracker.backend.web.controller;

import io.swagger.annotations.ApiParam;
import org.aidtracker.backend.util.GlobalAuthUtil;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.DemandCreateRequest;
import org.aidtracker.backend.web.dto.DemandDTO;
import org.aidtracker.backend.web.dto.DemandUpdateRequest;
import org.aidtracker.backend.web.service.DemandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/public/demand/list")
    public SimpleResult<Page<DemandDTO>> getAllDemand(@RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int size) {
        return SimpleResult.success(demandService.allDemand(PageRequest.of(page, size)));
    }

    @PostMapping("/demand")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<DemandDTO> create(@RequestBody @ApiParam("DemandCreateRequest") DemandCreateRequest request) {
        return SimpleResult.success(demandService.create(request, GlobalAuthUtil.authedAccount()));
    }

    @PutMapping("/demand")
    @PreAuthorize("hasAnyAuthority('GRANTEE')")
    public SimpleResult<DemandDTO> update(@RequestBody @ApiParam("DemandUpdateRequest") DemandUpdateRequest request) {
        return SimpleResult.success(demandService.update(request, GlobalAuthUtil.authedAccount()));
    }

}
