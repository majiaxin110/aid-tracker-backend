package org.aidtracker.backend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aidtracker.backend.AccountEnvBaseTest;
import org.aidtracker.backend.dao.DemandRepository;
import org.aidtracker.backend.dao.SupplyProjectRepository;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.ContactTypeEnum;
import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.domain.supply.DeliverPeriodTypeEnum;
import org.aidtracker.backend.domain.supply.SupplyDeliverMethodEnum;
import org.aidtracker.backend.domain.supply.SupplyProject;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mtage
 * @since 2020/8/3 09:43
 */
@SpringBootTest
class SupplyProjectTrackControllerTest extends AccountEnvBaseTest {
    @Autowired
    SupplyProjectTrackController supplyProjectTrackController;
    @Autowired
    DemandRepository demandRepository;
    @Autowired
    SupplyProjectController supplyProjectController;
    @Autowired
    SupplyProjectRepository supplyProjectRepository;

    private Demand demand;
    private SupplyProject supplyProject;

    private final String VALID_EXPRESS_NUM = "75359661708153";
    private final String VALID_SFEXPRESS_NUM = "SF1025485543588";

    @BeforeEach
    void setUpSupplyProject() {
        setUpGranteeEnv();
        List<Demand> allTestDemand = demandRepository.findAllByAccountId(testGranteeAccount.getAccountId());
        demand = allTestDemand.get(0);

        setUpDonatorEnv();
        SupplyProjectCreateRequest supplyProjectCreateRequest = new SupplyProjectCreateRequest();
        supplyProjectCreateRequest.setAmount(BigDecimal.valueOf(5L));
        supplyProjectCreateRequest.setDeliverMethod(SupplyDeliverMethodEnum.DONATOR);
        supplyProjectCreateRequest.setDemandId(demand.getDemandId());
        supplyProjectCreateRequest.setGoods(demand.getGoods());
        supplyProjectCreateRequest.setContact(new Contact("Eyjafjalla", ContactTypeEnum.WECHAT, "ttt"));
        SimpleResult<SupplyProjectDTO> createResult = supplyProjectController.create(supplyProjectCreateRequest);

        supplyProject = supplyProjectRepository.findById(createResult.getResult().getSupplyProjectId()).get();

        setUpGranteeEnv();
        supplyProjectController.granteeAgree(supplyProject.getSupplyProjectId());
    }

    @Test
    void getAllLog() throws JsonProcessingException {
        SimpleResult<SupplyProjectAllLogDTO> result = supplyProjectTrackController.getAllLog(supplyProject.getSupplyProjectId());
        checkAndPrint(result);

        assertEquals(2, result.getResult().getLogs().size());
    }

    @Test
    void trackExpress() throws JsonProcessingException {
        DispatchRequest dispatchRequest = new DispatchRequest();
        dispatchRequest.setSupplyProjectId(supplyProject.getSupplyProjectId());

        List<DispatchRequest.DeliverPeriodInfo> deliverPeriodInfoList = new ArrayList<>();
        deliverPeriodInfoList.add(DispatchRequest.DeliverPeriodInfo.builder()
                .periodType(DeliverPeriodTypeEnum.DIY)
                .contact(Contact.builder().contactName("中间人").contactInfo("110").type(ContactTypeEnum.PHONE).build())
                .build());
        deliverPeriodInfoList.add(DispatchRequest.DeliverPeriodInfo.builder()
                .periodType(DeliverPeriodTypeEnum.EXPRESS)
                .trackingNum(VALID_EXPRESS_NUM)
                .build());
        deliverPeriodInfoList.add(DispatchRequest.DeliverPeriodInfo.builder()
                .periodType(DeliverPeriodTypeEnum.DIY)
                .contact(Contact.builder().contactName("终末人").contactInfo("120").type(ContactTypeEnum.PHONE).build())
                .build());
        dispatchRequest.setDeliverPeriodList(deliverPeriodInfoList);

        setUpDonatorEnv();
        supplyProjectController.donatorDispatch(dispatchRequest);

        SimpleResult<SupplyProjectTrackDTO> result = supplyProjectTrackController.trackExpress(supplyProject.getSupplyProjectId());
        checkAndPrint(result);
    }

    @Test
    void trackSFExpress() throws JsonProcessingException {
        DispatchRequest dispatchRequest = new DispatchRequest();
        dispatchRequest.setSupplyProjectId(supplyProject.getSupplyProjectId());

        List<DispatchRequest.DeliverPeriodInfo> deliverPeriodInfoList = new ArrayList<>();
        deliverPeriodInfoList.add(DispatchRequest.DeliverPeriodInfo.builder()
                .periodType(DeliverPeriodTypeEnum.EXPRESS)
                .trackingNum(VALID_SFEXPRESS_NUM)
                .build());
        dispatchRequest.setDeliverPeriodList(deliverPeriodInfoList);

        setUpDonatorEnv();
        supplyProjectController.donatorDispatch(dispatchRequest);

        SimpleResult<SupplyProjectTrackDTO> result = supplyProjectTrackController.trackExpress(supplyProject.getSupplyProjectId());
        checkAndPrint(result);
    }
}