package com.example.daobe.common.config;

import com.example.daobe.auth.oauth.CustomAuthorizationRequestRepository;
import com.example.daobe.auth.oauth.OAuthService;
import com.example.daobe.auth.oauth.OAuthSuccessHandler;
import com.example.daobe.auth.security.JwtAuthenticationFilter;
import com.example.daobe.auth.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuthService oAuthService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        RequestMatcher requestMatcher = generatedRequestMatcher();
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(requestMatcher);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    private RequestMatcher generatedRequestMatcher() {
        return new NegatedRequestMatcher(
                new OrRequestMatcher(
                        new AntPathRequestMatcher("/api/v1/health"),
                        new AntPathRequestMatcher("/api/v1/auth/reissue"),
                        new AntPathRequestMatcher("/oauth2/authorization/kakao")
                )
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager
    ) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/health").permitAll()
                        .requestMatchers("/api/v1/auth/reissue").permitAll()
                        .requestMatchers("/oauth2/authorization/kakao").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oAuthService)
                        )
                        .authorizationEndpoint(
                                authorizationEndpointConfig -> authorizationEndpointConfig.authorizationRequestRepository(
                                        customAuthorizationRequestRepository))
                        .successHandler(oAuthSuccessHandler)
                )
                .addFilterBefore(
                        jwtAuthenticationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authenticationProvider(jwtAuthenticationProvider)
                .build();
    }
}
