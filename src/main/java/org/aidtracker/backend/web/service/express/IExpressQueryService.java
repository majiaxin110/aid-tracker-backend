package org.aidtracker.backend.web.service.express;

import org.aidtracker.backend.web.dto.ExpressHistoryDTO;

/**
 * 此处直接将返回类型定为DTO 实际还是搞成领域内的模型比较好
 * @author mtage
 * @since 2020/8/2 14:30
 */
public interface IExpressQueryService {
    /**
     * 查询单个快递单号的物流信息
     * @param trackingNum
     * @param phoneNum 收件人/寄件人手机号
     * @return
     */
    ExpressHistoryDTO query(String trackingNum, String phoneNum);
}
