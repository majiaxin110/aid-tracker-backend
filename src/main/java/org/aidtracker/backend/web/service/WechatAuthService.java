package org.aidtracker.backend.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 微信用户认证
 * @author mtage
 * @since 2020/7/27 11:13
 */
@Service
@ConfigurationProperties(prefix = "wechat.auth")
@Slf4j
public class WechatAuthService {
    @Value("url")
    private String url;

    @Value("appid")
    private String appId;

    @Value("secret")
    private String secret;

    private final String grantType = "authorization_code";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 进行微信小程序登录认证，由临时登录凭证code转换至用户唯一标识openId
     * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html">小程序登录</a>
     * @param jsCode 前端wx.login()获取的临时登录code
     * @return 用户唯一标识openId
     * @throws org.aidtracker.backend.util.CommonSysException code无效/微信服务器连不上时...
     */
    public String auth(String jsCode) {
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("appid", appId)
                .queryParam("secret", secret)
                .queryParam("grant_type", grantType)
                .queryParam("js_code", jsCode)
                .build().toUri();
        String getResult = restTemplate.getForObject(uri, String.class);
        try {
            JsonNode jsonNode = objectMapper.readTree(getResult);
            if (jsonNode.has("openid")) {
                return jsonNode.get("openid").asText();
            }
            if (jsonNode.has("errcode")) {
                throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), "无效的微信 wechat code " + jsonNode.get("errmsg").asText());
            }
            throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), "无效的微信接口信息" + jsonNode.asText());
        } catch (JsonProcessingException e) {
            throw new CommonSysException(AidTrackerCommonErrorCode.SYSTEM_ERROR.getErrorCode(), e.getMessage(), e);
        }
    }
}
