package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.supply.SupplyProjectLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author mtage
 * @since 2020/8/1 10:38
 */
public interface SupplyProjectLogRepository extends JpaRepository<SupplyProjectLog, Long> {
    List<SupplyProjectLog> findAllBySupplyProjectId(long supplyProjectId);
}
