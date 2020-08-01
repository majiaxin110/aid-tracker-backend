package org.aidtracker.backend.web.service;

import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.dao.DemandRepository;
import org.aidtracker.backend.dao.SupplyProjectLogRepository;
import org.aidtracker.backend.dao.SupplyProjectRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.domain.supply.SupplyProject;
import org.aidtracker.backend.domain.supply.SupplyProjectLog;
import org.aidtracker.backend.domain.supply.SupplyProjectStatusEnum;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.web.dto.SupplyProjectCreateRequest;
import org.aidtracker.backend.web.dto.SupplyProjectDTO;
import org.aidtracker.backend.web.dto.SupplyProjectUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
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

    private final SupplyProjectLogRepository supplyProjectLogRepository;

    private final DemandRepository demandRepository;

    @Autowired
    public SupplyProjectService(SupplyProjectRepository supplyProjectRepository, SupplyProjectLogRepository supplyProjectLogRepository, DemandRepository demandRepository) {
        this.supplyProjectRepository = supplyProjectRepository;
        this.supplyProjectLogRepository = supplyProjectLogRepository;
        this.demandRepository = demandRepository;
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
        supplyProject.setGoods(request.getGoods());
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

    @Transactional(rollbackFor = Exception.class)
    public void granteeAgree(long supplyProjectId, Account granteeAccount) {
        SupplyProject supplyProject = supplyProjectRepository.findById(supplyProjectId).orElseThrow(() ->
                new CommonSysException(AidTrackerCommonErrorCode.NOT_FOUND.getErrorCode(), "未找到对应捐赠项目"));
        Demand demand = demandRepository.findById(supplyProject.getDemandId()).orElseThrow(() ->
                new CommonSysException(AidTrackerCommonErrorCode.SYSTEM_ERROR.getErrorCode(), "捐赠项目无正确关联的需求 " + supplyProjectId));
        if (demand.getAccountId() != granteeAccount.getAccountId()) {
            throw new CommonSysException(AidTrackerCommonErrorCode.FORBIDDEN.getErrorCode(), "无该项目权限");
        }

        SupplyProjectLog projectLog = supplyProject.granteeAgreed(granteeAccount);
        demand.confirm(supplyProject);

        supplyProjectRepository.save(supplyProject);
        supplyProjectLogRepository.save(projectLog);
        demandRepository.save(demand);
    }

}
