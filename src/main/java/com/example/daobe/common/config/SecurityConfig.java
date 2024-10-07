package com.example.daobe.common.config;

import com.example.daobe.auth.infrastructure.security.JwtAuthenticationFilter;
import com.example.daobe.auth.infrastructure.security.JwtAuthenticationProvider;
import com.example.daobe.auth.infrastructure.security.handler.CustomAccessDeniedHandler;
import com.example.daobe.auth.infrastructure.security.handler.CustomAuthenticationEntryPoint;
import com.example.daobe.auth.infrastructure.security.oauth.CustomAuthorizationRequestRepository;
import com.example.daobe.auth.infrastructure.security.oauth.OAuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String NOOP_ENCODER_PREFIX = "{noop}";

    private final ActuatorProperties actuatorProperties;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainBasic(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/actuator/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().hasRole(actuatorProperties.role()))
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
                .userDetailsService(userDetailsService())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChainJwt(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/init").permitAll()
                        .requestMatchers("/api/v1/health").permitAll()
                        .requestMatchers("/api/v1/auth/reissue").permitAll()
                        .requestMatchers("/oauth2/authorization/kakao").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(new DefaultOAuth2UserService())
                        )
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .authorizationRequestRepository(customAuthorizationRequestRepository)
                        )
                        .successHandler(oAuthSuccessHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
                .build();
    }

    private UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername(actuatorProperties.username())
                .password(NOOP_ENCODER_PREFIX + actuatorProperties.password())
                .roles(actuatorProperties.role())
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter() {
        ProviderManager providerManager = new ProviderManager(jwtAuthenticationProvider);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(generatedRequestMatcher());
        filter.setAuthenticationManager(providerManager);
        filter.setAuthenticationFailureHandler(new AuthenticationEntryPointFailureHandler(authenticationEntryPoint));
        return filter;
    }

    private RequestMatcher generatedRequestMatcher() {
        return new NegatedRequestMatcher(
                new OrRequestMatcher(
                        new AntPathRequestMatcher("/ws/init"),
                        new AntPathRequestMatcher("/api/v1/health"),
                        new AntPathRequestMatcher("/api/v1/auth/reissue"),
                        new AntPathRequestMatcher("/oauth2/authorization/kakao")
                )
        );
    }
}
