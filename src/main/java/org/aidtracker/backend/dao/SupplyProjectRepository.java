package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.supply.SupplyProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author mtage
 * @since 2020/7/29 10:39
 */
public interface SupplyProjectRepository extends JpaRepository<SupplyProject, Long> {
    List<SupplyProject> findAllByDemandId(long demandId);

    List<SupplyProject> findAllByAccountId(long accountId);

    Optional<SupplyProject> findBySupplyProjectIdAndAccountId(long supplyProjectId, long accountId);

}
