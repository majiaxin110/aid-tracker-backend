package org.aidtracker.backend.web.service.express;

import org.aidtracker.backend.web.dto.ExpressHistoryDTO;

/**
 * @author mtage
 * @since 2020/8/2 14:30
 */
public interface IExpressQueryService {
    ExpressHistoryDTO query(String trackingNum);
}
