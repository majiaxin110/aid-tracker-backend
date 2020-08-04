package org.aidtracker.backend.web.service;

import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.dao.*;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.domain.supply.*;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final DeliverPeriodRepository deliverPeriodRepository;

    private final AccountService accountService;

    private final CosFileService cosFileService;

    @Autowired
    public SupplyProjectService(SupplyProjectRepository supplyProjectRepository, SupplyProjectLogRepository supplyProjectLogRepository,
                                DemandRepository demandRepository, DeliverPeriodRepository deliverPeriodRepository, AccountService accountService, CosFileService cosFileService) {
        this.supplyProjectRepository = supplyProjectRepository;
        this.supplyProjectLogRepository = supplyProjectLogRepository;
        this.demandRepository = demandRepository;
        this.deliverPeriodRepository = deliverPeriodRepository;
        this.accountService = accountService;
        this.cosFileService = cosFileService;
    }

    @Transactional(rollbackFor = Exception.class)
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

        SupplyProjectLog projectLog = SupplyProjectLog.of(supplyProject, SupplyProjectLogTypeEnum.DONATE_SUBMIT);
        supplyProjectLogRepository.save(projectLog);

        return SupplyProjectDTO.fromSupplyProject(supplyProject, account);
    }

    public SupplyProjectDTO update(SupplyProjectUpdateRequest request, Account account) {
        SupplyProject supplyProject = findProjectByIdDonator(request.getSupplyProjectId(), account);
        supplyProject.setDeliverMethod(request.getDeliverMethod());
        supplyProject.setAddress(request.getAddress());
        supplyProject.setContact(request.getContact());
        supplyProject.setComment(request.getComment());

        supplyProject = supplyProjectRepository.save(supplyProject);

        return SupplyProjectDTO.fromSupplyProject(supplyProject, account);
    }

    /**
     * 根据id查询单个捐赠项目信息 将校验相关权限
     * @param supplyProjectId
     * @param account
     * @return
     */
    public SupplyProjectDTO getById(long supplyProjectId, Account account) {
        SupplyProject supplyProject = findProjectByIdAccount(supplyProjectId, account);
        if (supplyProject.getAccountId() == account.getAccountId()) {
            return SupplyProjectDTO.fromSupplyProject(supplyProject, recentLog(supplyProject), account);
        } else {
            return SupplyProjectDTO.fromSupplyProject(supplyProject, recentLog(supplyProject), accountService.getById(supplyProject.getAccountId()));
        }
    }

    public Map<SupplyProjectStatusEnum, List<SupplyProjectDTO>> allSupplyProjectByAccount(Account account) {
        return supplyProjectRepository.findAllByAccountId(account.getAccountId()).stream()
                .sorted(Comparator.comparing(SupplyProject::getApplyTime).reversed())
                .map(s -> SupplyProjectDTO.fromSupplyProject(s, account))
                .collect(Collectors.groupingBy(SupplyProjectDTO::getStatus, Collectors.toList()));
    }

    /**
     * 某个需求的全部相关项目，按项目状态分隔
     * @param demand
     * @return
     */
    public Map<SupplyProjectStatusEnum, List<SupplyProjectDTO>> allSupplyProjectByDemand(Demand demand) {
        return supplyProjectRepository.findAllByDemandId(demand.getDemandId()).stream()
                .sorted(Comparator.comparing(SupplyProject::getApplyTime).reversed())
                .map(s -> SupplyProjectDTO.fromSupplyProject(s, recentLog(s), accountService.getById(s.getAccountId())))
                .collect(Collectors.groupingBy(SupplyProjectDTO::getStatus, Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void granteeAgree(long supplyProjectId, Account granteeAccount) {
        SupplyProject supplyProject = findById(supplyProjectId);
        Demand demand = findGranteeDemand(supplyProject, granteeAccount);
        if (demand.getAccountId() != granteeAccount.getAccountId()) {
            throw new CommonSysException(AidTrackerCommonErrorCode.FORBIDDEN.getErrorCode(), "无该项目权限");
        }

        SupplyProjectLog projectLog = supplyProject.granteeAgreed(granteeAccount);
        demand.agree(supplyProject);

        supplyProjectRepository.save(supplyProject);
        supplyProjectLogRepository.save(projectLog);
        demandRepository.save(demand);
    }

    @Transactional(rollbackFor = Exception.class)
    public void donatorDispatch(DispatchRequest request, Account donatorAccount) {
        SupplyProject supplyProject = findProjectByIdDonator(request.getSupplyProjectId(), donatorAccount);
        List<DeliverPeriod> deliverPeriodList = request.getDeliverPeriodList().stream().map(p -> {
            DeliverPeriod deliverPeriod = new DeliverPeriod();
            deliverPeriod.setContact(p.getContact());
            deliverPeriod.setPeriodType(p.getPeriodType());
            deliverPeriod.setTrackingNum(p.getTrackingNum());
            deliverPeriod.setSupplyProjectId(supplyProject.getSupplyProjectId());
            deliverPeriodRepository.save(deliverPeriod);
            return deliverPeriod;
        }).collect(Collectors.toList());
        SupplyProjectLog projectLog = supplyProject.dispatch(deliverPeriodList);

        supplyProjectRepository.save(supplyProject);
        supplyProjectLogRepository.save(projectLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void granteeReceived(GranteeReceivedRequest request, Account granteeAccount) {
        SupplyProject supplyProject = findById(request.getSupplyProjectId());
        Demand demand = findGranteeDemand(supplyProject, granteeAccount);

        SupplyProjectLog projectLog = supplyProject.granteeReceived(request.getFileIds());
        supplyProjectRepository.save(supplyProject);
        supplyProjectLogRepository.save(projectLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void granteeSubmitCert(DonateCertRequest request, Account granteeAccount) {
        SupplyProject supplyProject = findById(request.getSupplyProjectId());
        Demand demand = findGranteeDemand(supplyProject, granteeAccount);

        SupplyProjectLog projectLog = supplyProject.donateCert(request.getFileIds());
        supplyProjectRepository.save(supplyProject);
        supplyProjectLogRepository.save(projectLog);
    }


    public SupplyProject findById(long supplyProjectId) {
        return supplyProjectRepository.findById(supplyProjectId).orElseThrow(() ->
                new CommonSysException(AidTrackerCommonErrorCode.NOT_FOUND.getErrorCode(), "未找到对应捐赠项目"));
    }

    public SupplyProject findProjectByIdDonator(long supplyProjectId, Account donatorAccount) {
        return supplyProjectRepository.findBySupplyProjectIdAndAccountId(supplyProjectId,
                donatorAccount.getAccountId()).orElseThrow(() -> new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), "不存在的捐赠项目id"));
    }

    public SupplyProject findProjectByIdAccount(long supplyProjectId, Account account) {
        SupplyProject supplyProject = findById(supplyProjectId);
        if (supplyProject.getAccountId() != account.getAccountId()) {
            findGranteeDemand(supplyProject, account);
        }
        return supplyProject;
    }

    /**
     * 找到某个捐赠项目所关联的需求
     * @param supplyProject
     * @param granteeAccount
     * @throws CommonSysException (FORBIDDEN) 校验granteeAccount管理该需求权限失败
     * @return
     */
    private Demand findGranteeDemand(SupplyProject supplyProject, Account granteeAccount) {
        Demand demand = demandRepository.findById(supplyProject.getDemandId()).orElseThrow(() ->
                new CommonSysException(AidTrackerCommonErrorCode.SYSTEM_ERROR.getErrorCode(), "捐赠项目无正确关联的需求 " + supplyProject.getSupplyProjectId()));
        if (demand.getAccountId() != granteeAccount.getAccountId()) {
            throw new CommonSysException(AidTrackerCommonErrorCode.FORBIDDEN.getErrorCode(), "无该项目权限");
        }
        return demand;
    }


    private SupplyProjectLogDTO recentLog(SupplyProject supplyProject) {
        List<SupplyProjectLog> allLog = supplyProjectLogRepository.findAllBySupplyProjectId(supplyProject.getSupplyProjectId());
        return allLog.stream().max(Comparator.comparing(SupplyProjectLog::getTime))
                .map(l -> SupplyProjectLogDTO.fromSupplyProjectLog(l, cosFileService.getByIdList(l.getFileIds())))
                .orElse(null);
    }

}
