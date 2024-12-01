package ru.jetlabs.ts.clientwebbff.config;

import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.jetlabs.ts.clientwebbff.entities.ValidationResult;
import ru.jetlabs.ts.clientwebbff.service.AuthService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Configuration
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(authService));
        registrationBean.addUrlPatterns("/bff/api/v1/secured/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<ConfirmedEmailFilter> confirmedEmailFilter() {
        FilterRegistrationBean<ConfirmedEmailFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ConfirmedEmailFilter(authService));
        registrationBean.addUrlPatterns("/bff/api/v1/confirmed/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    public static class JwtAuthenticationFilter extends OncePerRequestFilter {
        private final AuthService authService;

        private JwtAuthenticationFilter(AuthService authService) {
            this.authService = authService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            if (request.getRequestURI().startsWith("/bff/api/v1/secured/")) {
                Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies())
                        .filter(cookie -> "jwt".equals(cookie.getName()))
                        .findFirst();

                if (jwtCookie.isPresent()) {
                    String token = jwtCookie.get().getValue();
                    ValidationResult result;
                    try {
                        result = authService.validate(token);
                    }catch (FeignException e){
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error in system");
                        return;
                    }
                    if (result.isValid()) {
                        Cookie newJwtCookie = new Cookie("jwt", result.newToken());
                        newJwtCookie.setHttpOnly(true);
                        response.addCookie(newJwtCookie);
                        request.setAttribute("extractedId", result.userId());
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                        return;
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    public static class ConfirmedEmailFilter extends OncePerRequestFilter {
        private final AuthService authService;

        private ConfirmedEmailFilter(AuthService authService) {
            this.authService = authService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            if (request.getRequestURI().startsWith("/bff/api/v1/confirmed/")) {
                Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies())
                        .filter(cookie -> "jwt".equals(cookie.getName()))
                        .findFirst();

                if (jwtCookie.isPresent()) {
                    String token = jwtCookie.get().getValue();
                    ValidationResult result;
                    try {
                        result = authService.validateConfirmed(token);
                    }catch (FeignException e){
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error in system");
                        return;
                    }
                    if (result.isValid()) {
                        Cookie newJwtCookie = new Cookie("jwt", result.newToken());
                        newJwtCookie.setHttpOnly(true);
                        response.addCookie(newJwtCookie);
                        request.setAttribute("extractedId", result.userId());
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Need email confirm");
                        return;
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Need email confirm");
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}