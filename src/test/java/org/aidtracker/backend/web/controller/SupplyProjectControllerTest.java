package org.aidtracker.backend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.AccountEnvBaseTest;
import org.aidtracker.backend.dao.SupplyProjectRepository;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;
import org.aidtracker.backend.domain.supply.SupplyDeliverMethodEnum;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.AccountDTO;
import org.aidtracker.backend.web.dto.SupplyProjectCreateRequest;
import org.aidtracker.backend.web.dto.SupplyProjectDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    SupplyProjectRepository repository;

    @Test
    void create() throws JsonProcessingException {
        Goods goods = new Goods("手办", "精品233", "万代", "个");
        SupplyProjectCreateRequest request = new SupplyProjectCreateRequest();
        request.setAccount(AccountDTO.fromAccount(testAccount));
        request.setGoods(goods);
        request.setAddress(new DeliverAddress("200000", "上海黄浦江"));
        request.setDemandId(1L);
        request.setDeliverMethod(SupplyDeliverMethodEnum.DONATOR);
        request.setAmount(BigDecimal.TEN);

        SimpleResult<SupplyProjectDTO> result = supplyProjectController.create(request);

        assertNotNull(result);
        Long supplyProjectId = result.getResult().getSupplyProjectId();
        assertNotNull(supplyProjectId);
        assertNotNull(repository.findById(supplyProjectId));

        printResult(result);
    }
}