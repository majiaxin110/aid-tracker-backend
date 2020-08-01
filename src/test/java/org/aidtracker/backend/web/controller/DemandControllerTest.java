package org.aidtracker.backend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aidtracker.backend.AccountEnvBaseTest;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.ContactTypeEnum;
import org.aidtracker.backend.domain.Goods;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.dto.DemandCreateRequest;
import org.aidtracker.backend.web.dto.DemandDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mtage
 * @since 2020/8/1 11:04
 */
class DemandControllerTest extends AccountEnvBaseTest {
    @Autowired
    DemandController demandController;

    @Test
    void create() throws JsonProcessingException {
        setUpGranteeEnv();

        DemandCreateRequest request = new DemandCreateRequest();
        request.setGoods(new Goods("京吹蓝光盘", "BD", "Kyoto Animation", "套"));
        request.setTopic("急求京吹蓝光盘");
        request.setContact(new Contact("Tomoyo", ContactTypeEnum.PHONE, "101100"));
        request.setAmount(BigDecimal.TEN);

        SimpleResult<DemandDTO> result = demandController.create(request);
        assertNotNull(result);
        assertTrue(result.getSuccess());
        printResult(result);
    }
}