package org.aidtracker.backend.web.service;

import org.aidtracker.backend.dao.DemandRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.web.dto.DemandCreateRequest;
import org.aidtracker.backend.web.dto.DemandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mtage
 * @since 2020/7/28 13:42
 */
@Service
public class DemandService {
    private final DemandRepository demandRepository;

    @Autowired
    public DemandService(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
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
     * 全部需求列表
     * TODO：排序方式
     * @param pageable
     * @return
     */
    public Page<DemandDTO> allDemand(Pageable pageable) {
        return demandRepository.findAll(pageable).map(DemandDTO::fromDemand);
    }

}
