package org.aidtracker.backend.web.service;

import com.google.common.collect.Lists;
import org.aidtracker.backend.dao.DeliverPeriodRepository;
import org.aidtracker.backend.dao.SupplyProjectLogRepository;
import org.aidtracker.backend.dao.SupplyProjectRepository;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.domain.supply.DeliverPeriod;
import org.aidtracker.backend.domain.supply.DeliverPeriodTypeEnum;
import org.aidtracker.backend.domain.supply.SupplyProject;
import org.aidtracker.backend.domain.supply.SupplyProjectLog;
import org.aidtracker.backend.web.dto.DeliverPeriodDTO;
import org.aidtracker.backend.web.dto.ExpressHistoryDTO;
import org.aidtracker.backend.web.dto.SupplyProjectAllLogDTO;
import org.aidtracker.backend.web.dto.SupplyProjectTrackDTO;
import org.aidtracker.backend.web.service.express.IExpressQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author mtage
 * @since 2020/8/2 11:02
 */
@Service
public class SupplyProjectTrackService {
    private final SupplyProjectRepository supplyProjectRepository;
    private final SupplyProjectLogRepository supplyProjectLogRepository;
    private final SupplyProjectService supplyProjectService;
    private final CosFileService cosFileService;
    private final IExpressQueryService expressQueryService;
    private final DemandService demandService;
    private final DeliverPeriodRepository deliverPeriodRepository;

    @Autowired
    public SupplyProjectTrackService(SupplyProjectRepository supplyProjectRepository, SupplyProjectLogRepository supplyProjectLogRepository, SupplyProjectService supplyProjectService, CosFileService cosFileService, IExpressQueryService expressQueryService, DemandService demandService, DeliverPeriodRepository deliverPeriodRepository) {
        this.supplyProjectRepository = supplyProjectRepository;
        this.supplyProjectLogRepository = supplyProjectLogRepository;
        this.supplyProjectService = supplyProjectService;
        this.cosFileService = cosFileService;
        this.expressQueryService = expressQueryService;
        this.demandService = demandService;
        this.deliverPeriodRepository = deliverPeriodRepository;
    }

    public SupplyProjectAllLogDTO allLogById(long supplyProjectId, Account account) {
        SupplyProject supplyProject = supplyProjectService.findProjectByIdAccount(supplyProjectId, account);
        List<SupplyProjectLog> allLog = supplyProjectLogRepository.findAllBySupplyProjectId(supplyProjectId);
        SupplyProjectAllLogDTO result = new SupplyProjectAllLogDTO();
        List<SupplyProjectAllLogDTO.LogInfo> allLogDTO = allLog.stream()
                .sorted(Comparator.comparing(SupplyProjectLog::getTime))
                .map(eachLog -> {
                    SupplyProjectAllLogDTO.LogInfo logDTO = new SupplyProjectAllLogDTO.LogInfo();
                    logDTO.setLogId(eachLog.getLogId());
                    logDTO.setFiles(eachLog.getFileIds().stream().map(cosFileService::getById).collect(Collectors.toList()));
                    logDTO.setInfo(eachLog.getInfo());
                    logDTO.setLogType(eachLog.getLogType());
                    logDTO.setSupplyProjectId(supplyProjectId);
                    logDTO.setTime(eachLog.getTime());
                    return logDTO;
                }).collect(Collectors.toList());
        result.setSupplyProjectId(supplyProjectId);
        result.setLogs(allLogDTO);
        return result;
    }

    public SupplyProjectTrackDTO trackExpressById(long supplyProjectId, Account account) {
        SupplyProject supplyProject = supplyProjectService.findProjectByIdAccount(supplyProjectId, account);
        List<DeliverPeriod> allDeliverPeriod = deliverPeriodRepository.findAllBySupplyProjectId(supplyProjectId);
        SupplyProjectTrackDTO result = new SupplyProjectTrackDTO();
        result.setSupplyProjectId(supplyProjectId);
        result.setDeliverPeriods(allDeliverPeriod.stream().map(DeliverPeriodDTO::fromDeliverPeriod).collect(Collectors.toList()));
        Demand demand = demandService.findById(supplyProject.getDemandId());
        // 从收货人/寄件人信息中提取手机号
        String phoneNum = Contact.getPhoneNumFromList(List.of(supplyProject.getContact(), demand.getContact()));
        for (DeliverPeriod eachPeriod : Lists.reverse(allDeliverPeriod)) {
            if (eachPeriod.getPeriodType() != DeliverPeriodTypeEnum.EXPRESS) {
                continue;
            }
            ExpressHistoryDTO expressHistoryDTO = expressQueryService.query(eachPeriod.getTrackingNum(), phoneNum);
            if (expressHistoryDTO.getValid()) {
                result.setExpressHistory(expressHistoryDTO);
            }
        }
        return result;
    }

}
