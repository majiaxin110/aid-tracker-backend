package org.aidtracker.backend.web.controller;

import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.util.SimpleResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mtage
 * @since 2020/7/27 14:50
 */
@RestController
public class DemandController {

    @GetMapping("/public/demand/list")
    public SimpleResult<List<Demand>> getAllDemand() {
        Demand demand = new Demand();
        demand.setTopic("test");
        return SimpleResult.success(List.of(demand));
    }
}
