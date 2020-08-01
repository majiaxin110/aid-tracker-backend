package org.aidtracker.backend.dao;

import org.aidtracker.backend.domain.CosFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mtage
 * @since 2020/7/30 16:32
 */
public interface CosFileRepository extends JpaRepository<CosFile, Long> {
}
