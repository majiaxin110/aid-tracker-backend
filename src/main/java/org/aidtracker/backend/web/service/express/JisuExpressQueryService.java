package org.aidtracker.backend.web.service.express;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.web.dto.ExpressHistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * @author mtage
 * @since 2020/8/2 14:25
 */
@Service
@Slf4j
public class JisuExpressQueryService implements IExpressQueryService {
    @Value("${express.jisu.url}")
    private String url;

    @Value("${express.jisu.type}")
    private String type;

    @Value("${express.jisu.appkey}")
    private String appkey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public static final String JISU_MSG = "msg";
    public static final String JISU_OK = "ok";

    @Autowired
    public JisuExpressQueryService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public ExpressHistoryDTO query(String trackingNum) {
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("type", type)
                .queryParam("appkey", appkey)
                .queryParam("number", trackingNum)
                .build().toUri();
        String infoStr = restTemplate.getForObject(uri, String.class);
        ExpressHistoryDTO result = new ExpressHistoryDTO();
        result.setTrackingNum(trackingNum);
        try {
            JsonNode infoJson = objectMapper.readTree(infoStr);
            if (!infoJson.get(JISU_MSG).asText().equalsIgnoreCase(JISU_OK)) {
                result.setValid(false);
                return result;
            }
            result.setValid(true);
            JsonNode innerInfoJson = infoJson.get("result");
            result.setLogUrl(innerInfoJson.get("logo").asText());
            ArrayList<ExpressHistoryDTO.HistoryInfo> historyList = new ArrayList<>();
            innerInfoJson.get("list").forEach(eachHistory -> {
                ExpressHistoryDTO.HistoryInfo historyInfo = new ExpressHistoryDTO.HistoryInfo();
                historyInfo.setStatusDesc(eachHistory.get("status").asText());
                historyInfo.setTime(LocalDateTime.parse(eachHistory.get("time").asText(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault()));
                historyList.add(historyInfo);
            });
            result.setList(historyList);
        } catch (JsonProcessingException e) {
            throw new CommonSysException(AidTrackerCommonErrorCode.SYSTEM_ERROR.getErrorCode(), "无效的物流接口信息" + infoStr);
        }
        return result;
    }
}
