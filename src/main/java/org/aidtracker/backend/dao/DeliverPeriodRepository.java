package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.supply.DeliverPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author mtage
 * @since 2020/8/1 12:11
 */
public interface DeliverPeriodRepository extends JpaRepository<DeliverPeriod, Long> {
    List<DeliverPeriod> findAllBySupplyProjectId(long supplyProjectId);
}
