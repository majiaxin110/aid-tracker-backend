package org.aidtracker.backend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.AccountEnvBaseTest;
import org.aidtracker.backend.dao.DeliverPeriodRepository;
import org.aidtracker.backend.dao.SupplyProjectRepository;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.ContactTypeEnum;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;
import org.aidtracker.backend.domain.supply.*;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mtage
 * @since 2020/7/29 11:02
 */
@SpringBootTest
@Slf4j
class SupplyProjectControllerTest extends AccountEnvBaseTest {
    @Autowired
    SupplyProjectController supplyProjectController;

    @Autowired
    SupplyProjectRepository supplyProjectRepository;

    @Autowired
    DeliverPeriodRepository deliverPeriodRepository;

    @Test
    void create() throws JsonProcessingException {
        Goods goods = new Goods("手办", "精品233", "万代", "个");
        SupplyProjectCreateRequest request = new SupplyProjectCreateRequest();
        request.setGoods(goods);
        request.setAddress(new DeliverAddress("200000", "上海黄浦江"));
        request.setDemandId(1L);
        request.setDeliverMethod(SupplyDeliverMethodEnum.DONATOR);
        request.setAmount(BigDecimal.TEN);

        SimpleResult<SupplyProjectDTO> result = supplyProjectController.create(request);

        assertNotNull(result);
        Long supplyProjectId = result.getResult().getSupplyProjectId();
        assertNotNull(supplyProjectId);
        assertNotNull(supplyProjectRepository.findById(supplyProjectId));

        printResult(result);
    }


    @Test
    void update() {
    }

    @Test
    void granteeAgree() throws JsonProcessingException {
        Goods goods = new Goods("蓝光盘", "BD", "Kyoto Animation", "套");
        SupplyProjectCreateRequest request = new SupplyProjectCreateRequest();
        request.setGoods(goods);
        request.setAddress(new DeliverAddress("200000", "上海黄浦江"));
        request.setDemandId(2L);
        request.setDeliverMethod(SupplyDeliverMethodEnum.DONATOR);
        request.setAmount(BigDecimal.TEN);
        SimpleResult<SupplyProjectDTO> createResult = supplyProjectController.create(request);
        Long supplyProjectId = createResult.getResult().getSupplyProjectId();

        setUpGranteeEnv();

        SimpleResult<String> result = supplyProjectController.granteeAgree(supplyProjectId);
        assertNotNull(result);
        assertTrue(result.getSuccess());
        printResult(result);

        SupplyProject supplyProject = supplyProjectRepository.findById(supplyProjectId).orElseThrow();
        assertEquals(SupplyProjectStatusEnum.GRANTEE_REPLY, supplyProject.getStatus());
    }

    @Test
    void donatorDispatch() throws JsonProcessingException {
        Goods goods = new Goods("蓝光盘", "BD", "Kyoto Animation", "套");
        SupplyProjectCreateRequest request = new SupplyProjectCreateRequest();
        request.setGoods(goods);
        request.setAddress(new DeliverAddress("200000", "上海黄浦江"));
        request.setDemandId(2L);
        request.setDeliverMethod(SupplyDeliverMethodEnum.DONATOR);
        request.setAmount(BigDecimal.TEN);
        SimpleResult<SupplyProjectDTO> createResult = supplyProjectController.create(request);
        Long supplyProjectId = createResult.getResult().getSupplyProjectId();

        setUpGranteeEnv();

        SimpleResult<String> agreeResult = supplyProjectController.granteeAgree(supplyProjectId);
        checkAndPrint(agreeResult);

        setUpDonatorEnv();

        DispatchRequest dispatchRequest = new DispatchRequest();
        dispatchRequest.setSupplyProjectId(supplyProjectId);
        DispatchRequest.DeliverPeriodInfo deliverPeriodInfoA = DispatchRequest.DeliverPeriodInfo.builder()
                .periodType(DeliverPeriodTypeEnum.EXPRESS)
                .trackingNum(RandomStringUtils.random(20))
                .build();
        DispatchRequest.DeliverPeriodInfo deliverPeriodInfoB = DispatchRequest.DeliverPeriodInfo.builder()
                .periodType(DeliverPeriodTypeEnum.DIY)
                .trackingNum(RandomStringUtils.random(20))
                .contact(new Contact("企鹅物流", ContactTypeEnum.PHONE, "110"))
                .build();
        dispatchRequest.setDeliverPeriodList(Lists.newArrayList(deliverPeriodInfoA, deliverPeriodInfoB));

        SimpleResult<String> result = supplyProjectController.donatorDispatch(dispatchRequest);
        checkAndPrint(result);

        SupplyProject supplyProject = supplyProjectRepository.findById(supplyProjectId).get();
        assertEquals(SupplyProjectStatusEnum.LOGISTICS_TRACKING, supplyProject.getStatus());

        List<DeliverPeriod> deliverPeriodList = deliverPeriodRepository.findAllBySupplyProjectId(supplyProjectId);
        assertEquals(2, deliverPeriodList.size());
    }

    @Test
    void granteeSubmitCert() throws JsonProcessingException, InterruptedException {
        Goods goods = new Goods("蓝光盘", "BD", "Kyoto Animation", "套");
        SupplyProjectCreateRequest projectCreateRequest = new SupplyProjectCreateRequest();
        projectCreateRequest.setGoods(goods);
        projectCreateRequest.setAddress(new DeliverAddress("200000", "上海黄浦江"));
        projectCreateRequest.setDemandId(2L);
        projectCreateRequest.setDeliverMethod(SupplyDeliverMethodEnum.DONATOR);
        projectCreateRequest.setAmount(BigDecimal.TEN);
        SimpleResult<SupplyProjectDTO> createResult = supplyProjectController.create(projectCreateRequest);
        Long supplyProjectId = createResult.getResult().getSupplyProjectId();

        setUpGranteeEnv();

        SimpleResult<String> agreeResult = supplyProjectController.granteeAgree(supplyProjectId);
        checkAndPrint(agreeResult);

        setUpDonatorEnv();

        DispatchRequest dispatchRequest = new DispatchRequest();
        dispatchRequest.setSupplyProjectId(supplyProjectId);
        DispatchRequest.DeliverPeriodInfo deliverPeriodInfoA = DispatchRequest.DeliverPeriodInfo.builder()
                .periodType(DeliverPeriodTypeEnum.EXPRESS)
                .trackingNum(RandomStringUtils.random(20))
                .build();
        dispatchRequest.setDeliverPeriodList(Lists.newArrayList(deliverPeriodInfoA));

        SimpleResult<String> dispatchResult = supplyProjectController.donatorDispatch(dispatchRequest);
        checkAndPrint(dispatchResult);

        Thread.sleep(1000);
        setUpGranteeEnv();

        GranteeReceivedRequest receivedRequest = new GranteeReceivedRequest();
        receivedRequest.setSupplyProjectId(supplyProjectId);
        receivedRequest.setFileIds(Lists.newArrayList(1L));
        SimpleResult<String> receivedResult = supplyProjectController.granteeReceived(receivedRequest);
        checkAndPrint(receivedResult);

        DonateCertRequest donateCertRequest = new DonateCertRequest();
        donateCertRequest.setSupplyProjectId(supplyProjectId);
        donateCertRequest.setFileIds(Lists.newArrayList(2L, 5L));
        SimpleResult<String> certResult = supplyProjectController.granteeSubmitCert(donateCertRequest);
        checkAndPrint(certResult);
    }
}