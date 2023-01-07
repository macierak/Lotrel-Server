package com.lotrel.ltserwer.authModule;

import com.lotrel.ltserwer.authModule.infrastructure.oauth2.TokenAuthenticationFilter;
import com.lotrel.ltserwer.authModule.infrastructure.oauth2.service.CustomOAuth2UserService;
import com.lotrel.ltserwer.authModule.infrastructure.oauth2.service.HttpCookieOAuth2AuthorizationRequestRepository;
import com.lotrel.ltserwer.authModule.infrastructure.oauth2.service.OAuth2AuthenticationFailureHandler;
import com.lotrel.ltserwer.authModule.infrastructure.oauth2.service.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig  {

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            /*
                .exceptionHandling()
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                    .formLogin()
                        .disable()*/
            .cors().and()
            .csrf().disable()
            .formLogin().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
            .authorizeHttpRequests()
                .requestMatchers("/auth/*", "/oauth2/*", "/login/oauth2/code/google", "/oauth2/callback/*")
                    .permitAll()
                .anyRequest()
                .authenticated().and()
            .oauth2Login()

                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

            http.addFilterBefore(tokenAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class);
        return http.build();
    }
}