package com.heeha.domain.auth.filter;

import com.heeha.domain.auth.jwt.JwtTokenExtractor;
import com.heeha.domain.auth.jwt.JwtTokenProvider;
import com.heeha.domain.customer.entity.RoleType;
import com.heeha.global.config.BaseException;
import com.heeha.global.config.BaseResponseStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
@Slf4j
@Component
public class CustomerAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final List<String> EXCLUDE_URL = List.of("/api/v1/auth", "/api/v1/customer/signup", "/favicon",
            "/api/v1/sms", "/swagger", "/v3", "/api/v1/developer", "/api/ocr");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("MemberAuthFilter Start : {}", request.getServletPath());

        String token = JwtTokenExtractor.extractJwt(request);
        RoleType roleType = tokenProvider.getRoleFromToken(token);
        Long id = tokenProvider.getCustomerIdFromToken(token);
        if (!roleType.equals(RoleType.USER)) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }
        request.setAttribute("id", id);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> request.getServletPath().startsWith(exclude));
    }
}