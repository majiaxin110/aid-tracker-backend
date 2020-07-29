package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.supply.SupplyProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author mtage
 * @since 2020/7/29 10:39
 */
public interface SupplyProjectRepository extends JpaRepository<SupplyProject, Long> {
    List<SupplyProject> getAllByDemandId(long demandId);

    List<SupplyProject> getAllByAccountId(long accountId);
}
