package org.aidtracker.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mtage
 * @since 2020/7/22 14:45
 */
@Component
public class JwtTokenUtil {
    private String secret = "jwt-aidtracker";
    private Long expiration = 3600000L;

    private final static String OPENID_KEY = "openid";
    private final static String ROLE_KEY = "role";
    /**
     * 生成token令牌
     *
     * @param account 用户
     * @return 令牌
     */
    public String generateToken(Account account) {
        Map<String, Object> claims = new HashMap<>(2);
        // username in subject
        claims.put(OPENID_KEY, account.getWechatOpenId());
        claims.put(ROLE_KEY, account.getRole().name());
        return Jwts.builder().setSubject(account.getName())
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getOpenIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Optional.ofNullable(claims.get(OPENID_KEY)).map(Object::toString).orElse(null);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public AccountRoleEnum getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Optional.ofNullable(claims.get(ROLE_KEY)).map(Object::toString).map(AccountRoleEnum::valueOf).orElse(null);
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param accountList 用户
     * @return 是否有效
     */
    public boolean validateToken(String token, List<Account> accountList) {
        String openId = getOpenIdFromToken(token);

        Set<String> accountOpenIdSet = accountList.stream().map(Account::getWechatOpenId).collect(Collectors.toSet());

        return (accountList.size() > 0) && !isTokenExpired(token) &&
                accountOpenIdSet.size() == 1 && accountOpenIdSet.contains(openId);
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @return 是否有效
     */
    public boolean validateToken(String token, Account account) {
        String openId = getOpenIdFromToken(token);
        AccountRoleEnum roleEnum = getRoleFromToken(token);

        return (StringUtils.equals(openId, account.getWechatOpenId())) && !isTokenExpired(token) &&
                roleEnum == account.getRole();
    }


    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }


    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}
