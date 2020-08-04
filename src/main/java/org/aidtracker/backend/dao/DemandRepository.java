package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.demand.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author mtage
 * @since 2020/7/25 12:49
 */
public interface DemandRepository extends JpaRepository<Demand, Long> {
    Optional<Demand> findByDemandIdAndAccountId(long demandId, long accountId);

    List<Demand> findAllByAccountId(long accountId);
}
