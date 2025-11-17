package historywowa.global.config;

import historywowa.global.infra.exception.auth.HistoryAuthExceptionFilter;
import historywowa.global.infra.filter.HistoryJWTFilter;
import historywowa.global.jwt.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private final List<String> excludedUrls = Arrays.asList(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/favicon.ico",
            "/api/app/reissue",
            "/api/oauth2/login/**",     // 새로운 토큰 방식 로그인 포함
            "/api/oauth2/callback/**",  // 기존 콜백 방식 (테스트용)
            "/api/healthcheck", "/api/admin/**","/api/test/**",
            "/actuator/**", "/api/callback/**"
    );

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("https://history.netlify.app"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(Collections.singletonList("Authorization"));
                    config.setMaxAge(3600L);
                    return config;
                }))
                .authorizeHttpRequests((url) -> url
                        .requestMatchers("/api/healthcheck").permitAll()
                        .requestMatchers("/api/oauth2/login/**")
                        .permitAll()     // POST /api/oauth2/login/{provider} 허용
                        .requestMatchers("/api/oauth2/callback/**","/api/test/**","/api/callback/**").permitAll()  // 기존 콜백 방식 허용
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/favicon.ico", "/api/region").permitAll()
                        .requestMatchers("/api/app/reissue").permitAll()
                        .requestMatchers("/api/web/reissue").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/app/public/**").permitAll()
                        .requestMatchers("/api/web/public/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(except -> except
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        ))

                .addFilterAfter(new HistoryAuthExceptionFilter(objectMapper), CorsFilter.class)
                .addFilterAfter(new HistoryJWTFilter(jwtUtil, excludedUrls),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}