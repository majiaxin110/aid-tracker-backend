package org.aidtracker.backend.web.dto;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;
import org.aidtracker.backend.domain.supply.SupplyDeliverMethodEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author mtage
 * @since 2020/7/29 10:40
 */
@Data
public class SupplyProjectCreateRequest {
    @NotNull
    private Long demandId;

    private Goods goods;

    private BigDecimal amount;

    private SupplyDeliverMethodEnum deliverMethod;

    private DeliverAddress address;

    private Contact contact;

    private String comment;
}
