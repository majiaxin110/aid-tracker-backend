package org.aidtracker.backend.web.dto;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;
import org.aidtracker.backend.domain.DeliverAddress;
import org.aidtracker.backend.domain.Goods;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author mtage
 * @since 2020/7/28 13:43
 */
@Data
public class DemandUpdateRequest {
    @NotNull
    private long demandId;

    private String topic;

    private String audiences;

    private Goods goods;

    private DeliverAddress address;

    private String selfTakeInfo;

    private Contact contact;

    private String comment;
}
