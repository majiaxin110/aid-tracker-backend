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
import java.util.stream.Collectors;

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

    private String tokenHeaderKey = "Authorization";
    private String tokenHead = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 取得header
        String authHeader = request.getHeader(this.tokenHeaderKey);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            // token 正式内容
            final String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUsernameFromToken(authToken);

            log.info("checking authentication " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                List<Account> accountList = accountRepository.findAllByWechatOpenId(username);

                if (jwtTokenUtil.validateToken(authToken, accountList)) {
                    // token 有效
                    List<String> roleList = accountList.stream()
                            .map(Account::getRole).map(AccountRoleEnum::name).collect(Collectors.toList());
                    log.info("authenticated user:{} openid:{} roles:{}",
                            username, jwtTokenUtil.getOpenIdFromToken(authToken), roleList);
                    List<SimpleGrantedAuthority> authorities = roleList.stream().map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(accountList, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
