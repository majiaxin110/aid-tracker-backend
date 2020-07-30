package org.aidtracker.backend.web.service;

import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.dao.SupplyProjectRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.supply.SupplyProject;
import org.aidtracker.backend.domain.supply.SupplyProjectStatusEnum;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.web.dto.SupplyProjectCreateRequest;
import org.aidtracker.backend.web.dto.SupplyProjectDTO;
import org.aidtracker.backend.web.dto.SupplyProjectUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 捐赠项目相关
 * @author mtage
 * @since 2020/7/29 10:38
 */
@Service
@Slf4j
public class SupplyProjectService {
    private final SupplyProjectRepository supplyProjectRepository;

    @Autowired
    public SupplyProjectService(SupplyProjectRepository supplyProjectRepository) {
        this.supplyProjectRepository = supplyProjectRepository;
    }

    public SupplyProjectDTO create(SupplyProjectCreateRequest request, Account account) {
        SupplyProject supplyProject = new SupplyProject();
        supplyProject.setAccountId(account.getAccountId());
        supplyProject.setDemandId(request.getDemandId());
        supplyProject.setAmount(request.getAmount());
        supplyProject.setStatus(SupplyProjectStatusEnum.DONATE_SUBMIT);
        supplyProject.setDeliverMethod(request.getDeliverMethod());
        supplyProject.setAddress(request.getAddress());
        supplyProject.setApplyTime(ZonedDateTime.now());
        supplyProject.setContact(request.getContact());
        supplyProject.setComment(request.getComment());

        supplyProject = supplyProjectRepository.save(supplyProject);

        return SupplyProjectDTO.fromSupplyProject(supplyProject, account);
    }

    public SupplyProjectDTO update(SupplyProjectUpdateRequest request, Account account) {
        SupplyProject supplyProject = supplyProjectRepository.findBySupplyProjectIdAndAccountId(request.getSupplyProjectId(),
                account.getAccountId());
        if (Objects.isNull(supplyProject)) {
            throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), "不存在的捐赠项目id");
        }
        supplyProject.setDeliverMethod(request.getDeliverMethod());
        supplyProject.setAddress(request.getAddress());
        supplyProject.setContact(request.getContact());
        supplyProject.setComment(request.getComment());

        supplyProject = supplyProjectRepository.save(supplyProject);

        return SupplyProjectDTO.fromSupplyProject(supplyProject, account);
    }

}
