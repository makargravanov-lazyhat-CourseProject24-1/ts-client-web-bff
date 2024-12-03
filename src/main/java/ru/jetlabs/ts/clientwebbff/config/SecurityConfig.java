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
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.jetlabs.ts.clientwebbff.entities.ValidationResult;
import ru.jetlabs.ts.clientwebbff.service.AuthService;
import ru.jetlabs.ts.clientwebbff.service.CookieUtility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
public class SecurityConfig {

    private final AuthService authService;
    private final CookieUtility cookieUtility;

    public SecurityConfig(AuthService authService, CookieUtility cookieUtility) {
        this.authService = authService;
        this.cookieUtility = cookieUtility;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(List.of("Set-Cookie", "Cookie"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(CorsConfiguration.ALL));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(1);

        return bean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(authService, cookieUtility));
        registrationBean.addUrlPatterns("/bff/api/v1/secured/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ConfirmedEmailFilter> confirmedEmailFilter() {
        FilterRegistrationBean<ConfirmedEmailFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ConfirmedEmailFilter(authService, cookieUtility));
        registrationBean.addUrlPatterns("/bff/api/v1/confirmed/*");
        registrationBean.setOrder(3);
        return registrationBean;
    }

    public static class JwtAuthenticationFilter extends OncePerRequestFilter {
        private final AuthService authService;
        private final CookieUtility cookieUtility;

        private JwtAuthenticationFilter(AuthService authService, CookieUtility cookieUtility) {
            this.authService = authService;
            this.cookieUtility = cookieUtility;
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
                    } catch (FeignException e) {
                        System.out.println("error in filter: " + e);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error in system");
                        return;
                    }
                    if (result.isValid()) {
                        response.setHeader(HttpHeaders.SET_COOKIE,
                                cookieUtility.create(result.newToken()).toString());
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
        private final CookieUtility cookieUtility;

        private ConfirmedEmailFilter(AuthService authService, CookieUtility cookieUtility) {
            this.authService = authService;
            this.cookieUtility = cookieUtility;
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
                    } catch (FeignException e) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error in system");
                        return;
                    }
                    if (result.isValid()) {
                        response.setHeader(HttpHeaders.SET_COOKIE,
                                cookieUtility.create(result.newToken()).toString());
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