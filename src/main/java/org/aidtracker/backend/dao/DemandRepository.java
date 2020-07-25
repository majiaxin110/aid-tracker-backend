package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.demand.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mtage
 * @since 2020/7/25 12:49
 */
public interface DemandRepository extends JpaRepository<Demand, Long> {
}
