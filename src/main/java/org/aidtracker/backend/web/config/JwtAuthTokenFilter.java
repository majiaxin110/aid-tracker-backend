package org.aidtracker.backend.web.config;

import lombok.extern.slf4j.Slf4j;
import org.aidtracker.backend.dao.AccountRepository;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.domain.account.AccountRoleEnum;
import org.aidtracker.backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * OncePerRequestFilter: Filter base class that aims to guarantee a single execution per request
 * dispatch, on any servlet container. It provides a {@link #doFilterInternal}
 * method with HttpServletRequest and HttpServletResponse arguments.
 * @author mtage
 * @since 2020/7/22 14:38
 */
@Component
@Slf4j
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AccountRepository accountRepository;

    public static final String TOKEN_HEADER_KEY = "Authorization";
    public static final String TOKEN_HEAD = "Bearer ";
    public static final String ROLE_PREFIX = "ROLE_";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 取得header
        String authHeader = request.getHeader(TOKEN_HEADER_KEY);
        if (authHeader != null && authHeader.startsWith(TOKEN_HEAD)) {
            // token 正式内容
            final String authToken = authHeader.substring(TOKEN_HEAD.length());
            String openId = jwtTokenUtil.getOpenIdFromToken(authToken);
            AccountRoleEnum roleEnum = jwtTokenUtil.getRoleFromToken(authToken);

            log.info("checking authentication {} role: {}", jwtTokenUtil.getUsernameFromToken(authToken), roleEnum);

            if (openId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Account account = accountRepository.getByWechatOpenIdAndRole(openId, roleEnum);

                if (jwtTokenUtil.validateToken(authToken, account)) {
                    // token 有效
                    log.info("authenticated user:{} openid:{} role:{}",
                            openId, jwtTokenUtil.getOpenIdFromToken(authToken), roleEnum);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(account, null,
                                    List.of(new SimpleGrantedAuthority(roleEnum.name())));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
