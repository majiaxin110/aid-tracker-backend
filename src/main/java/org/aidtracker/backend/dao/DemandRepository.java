package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.demand.Demand;
import org.aidtracker.backend.domain.demand.DemandStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author mtage
 * @since 2020/7/25 12:49
 */
public interface DemandRepository extends JpaRepository<Demand, Long> {
    Optional<Demand> findByDemandIdAndAccountId(long demandId, long accountId);

    List<Demand> findAllByAccountId(long accountId);

    Page<Demand> findAll(Specification<Demand> specification, Pageable pageable);

    int countByStatusNot(DemandStatusEnum filterStatus);

    @Query(value = "SELECT *,(round(6367000 * 2 * asin(sqrt(pow(sin(((deliver_address_lat * pi()) / 180 - (:lat * pi()) / 180) / 2), 2) + cos((:lat * pi()) / 180) * cos((:lat * pi()) / 180) * pow(sin(((deliver_address_lon * pi()) / 180 - (:lon * pi()) / 180) / 2), 2)))))  AS distance" +
            " FROM demand WHERE status != :filterStatus ORDER BY -distance DESC LIMIT :page, :size", nativeQuery = true)
    List<Demand> findAll(@Param("filterStatus") String filterStatus,
                         @Param("lat") BigDecimal lat, @Param("lon") BigDecimal lon, int page, int size);
}
