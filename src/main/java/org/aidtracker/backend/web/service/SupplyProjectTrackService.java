package org.aidtracker.backend.web.service;

import org.aidtracker.backend.dao.SupplyProjectLogRepository;
import org.aidtracker.backend.dao.SupplyProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mtage
 * @since 2020/8/2 11:02
 */
@Service
public class SupplyProjectTrackService {
    private final SupplyProjectRepository supplyProjectRepository;
    private final SupplyProjectLogRepository supplyProjectLogRepository;

    @Autowired
    public SupplyProjectTrackService(SupplyProjectRepository supplyProjectRepository, SupplyProjectLogRepository supplyProjectLogRepository) {
        this.supplyProjectRepository = supplyProjectRepository;
        this.supplyProjectLogRepository = supplyProjectLogRepository;
    }


}
