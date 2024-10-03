package com.example.daobe.common.config;

import com.example.daobe.auth.infrastructure.security.BasicAuthenticationProvider;
import com.example.daobe.auth.infrastructure.security.JwtAuthenticationFilter;
import com.example.daobe.auth.infrastructure.security.JwtAuthenticationProvider;
import com.example.daobe.auth.infrastructure.security.handler.CustomAccessDeniedHandler;
import com.example.daobe.auth.infrastructure.security.handler.CustomAuthenticationEntryPoint;
import com.example.daobe.auth.infrastructure.security.oauth.CustomAuthorizationRequestRepository;
import com.example.daobe.auth.infrastructure.security.oauth.OAuthSuccessHandler;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final BasicAuthenticationProvider basicAuthenticationProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(jwtAuthenticationProvider, basicAuthenticationProvider));
    }

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new BasicAuthenticationFilter(authenticationManager, authenticationEntryPoint);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(generatedRequestMatcher());
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationFailureHandler(
                new AuthenticationEntryPointFailureHandler(authenticationEntryPoint)
        );
        return filter;
    }

    private RequestMatcher generatedRequestMatcher() {
        return new NegatedRequestMatcher(
                new OrRequestMatcher(
                        new AntPathRequestMatcher("/actuator/**"),
                        new AntPathRequestMatcher("/api/v1/health"),
                        new AntPathRequestMatcher("/api/v1/auth/reissue"),
                        new AntPathRequestMatcher("/oauth2/authorization/kakao"),
                        new AntPathRequestMatcher("/ws/init")
                )
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChainJwt(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            BasicAuthenticationFilter basicAuthenticationFilter
    ) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .requestMatchers("/ws/init").permitAll()
                        .requestMatchers("/api/v1/health").permitAll()
                        .requestMatchers("/api/v1/auth/reissue").permitAll()
                        .requestMatchers("/oauth2/authorization/kakao").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(new DefaultOAuth2UserService())
                        )
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .authorizationRequestRepository(customAuthorizationRequestRepository)
                        )
                        .successHandler(oAuthSuccessHandler)
                )
                .addFilterAt(basicAuthenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
                .authenticationManager(authenticationManager())
                .build();
    }
}
