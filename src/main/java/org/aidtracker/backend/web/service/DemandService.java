package org.aidtracker.backend.web.service;

import org.aidtracker.backend.dao.DemandRepository;
import org.aidtracker.backend.dao.SupplyProjectRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.domain.demand.DemandStatusEnum;
import org.aidtracker.backend.domain.supply.SupplyProject;
import org.aidtracker.backend.domain.supply.SupplyProjectStatusEnum;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.web.dto.DemandCreateRequest;
import org.aidtracker.backend.web.dto.DemandDTO;
import org.aidtracker.backend.web.dto.DemandUpdateRequest;
import org.aidtracker.backend.web.dto.DemandWithSupplyCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author mtage
 * @since 2020/7/28 13:42
 */
@Service
public class DemandService {
    private final DemandRepository demandRepository;
    private final SupplyProjectRepository supplyProjectRepository;

    @Autowired
    public DemandService(DemandRepository demandRepository, SupplyProjectRepository supplyProjectRepository) {
        this.demandRepository = demandRepository;
        this.supplyProjectRepository = supplyProjectRepository;
    }

    /**
     * 新建单个需求
     * @param request
     * @param account
     * @return
     */
    public DemandDTO create(DemandCreateRequest request, Account account) {
        Demand demand = new Demand();
        demand.setAccountId(account.getAccountId());
        demand.setTopic(request.getTopic());
        demand.setAudiences(request.getAudiences());
        demand.setStatus(DemandStatusEnum.DEMAND_SUBMIT);
        demand.setAmount(request.getAmount());
        demand.setMetAmount(BigDecimal.ZERO);
        demand.setAddress(request.getAddress());
        demand.setSelfTakeInfo(request.getSelfTakeInfo());
        demand.setPublishTime(ZonedDateTime.now());
        demand.setContact(request.getContact());
        demand.setComment(request.getComment());

        demand = demandRepository.save(demand);

        return DemandDTO.fromDemand(demand);
    }

    /**
     * 更新单个需求的描述信息
     * @param request
     * @param account
     * @return
     */
    public DemandDTO update(DemandUpdateRequest request, Account account) {
        Demand demand = findByIdAccount(request.getDemandId(), account);

        demand.setAccountId(account.getAccountId());
        demand.setTopic(request.getTopic());
        demand.setAudiences(request.getAudiences());
        demand.setAddress(request.getAddress());
        demand.setSelfTakeInfo(request.getSelfTakeInfo());
        demand.setContact(request.getContact());
        demand.setComment(request.getComment());

        demand = demandRepository.save(demand);
        return DemandDTO.fromDemand(demand);
    }

    /**
     * 全部需求列表
     * TODO：排序方式
     * @param pageable
     * @return
     */
    public Page<DemandDTO> allDemand(Pageable pageable) {
        return demandRepository.findAll(pageable).map(DemandDTO::fromDemand);
    }

    /**
     * 单个账户全部的需求 不包含已关闭（删除）的需求
     * @param account
     * @return 按需求状态分
     */
    public Map<DemandStatusEnum, List<DemandWithSupplyCountDTO>> allDemandByAccount(Account account) {
        List<Demand> allDemand = demandRepository.findAllByAccountId(account.getAccountId());
        return allDemand.stream()
                .filter(demand -> demand.getStatus() != DemandStatusEnum.CLOSED)
                .sorted(Comparator.comparing(Demand::getPublishTime).reversed())
                .map(this::buildDemandWithSupplyProjectCount)
                .collect(Collectors.groupingBy(dto -> dto.getDemand().getStatus(), Collectors.toList()));
    }

    public Demand findById(long demandId) {
        return demandRepository.findById(demandId).orElseThrow(() ->
                new CommonSysException(AidTrackerCommonErrorCode.NOT_FOUND.getErrorCode(), "未找到对应需求"));
    }

    public Demand findByIdAccount(long demandId, Account granteeAccount) {
        return demandRepository.findByDemandIdAndAccountId(demandId, granteeAccount.getAccountId())
                .orElseThrow(() -> new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), "不存在的需求id"));
    }

    private DemandWithSupplyCountDTO buildDemandWithSupplyProjectCount(Demand demand) {
        List<SupplyProject> allSupplyProject = supplyProjectRepository.findAllByDemandId(demand.getDemandId());
        Map<SupplyProjectStatusEnum, Long> supplyProjectCount = allSupplyProject.stream()
                .collect(Collectors.groupingBy(SupplyProject::getStatus, Collectors.counting()));
        return DemandWithSupplyCountDTO.fromDemand(demand, supplyProjectCount);
    }

}
