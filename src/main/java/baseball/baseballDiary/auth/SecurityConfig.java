package baseball.baseballDiary.auth;

import baseball.baseballDiary.auth.service.CustomOauthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOauthUserService customOauthUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((session) ->
                        // 세션 방식 X
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(((authorizeRequests) ->
                        // 나중에 수정 필요
                        authorizeRequests.anyRequest().permitAll()
                ))
                .formLogin(AbstractHttpConfigurer::disable)  // form 로그인 해제
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logoutConfig) ->
                        logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessHandler(((request, response, authentication) ->
                                        response.setStatus(200)
                                ))
                )
                .oauth2Login((oauth) ->
                        oauth
                                .userInfoEndpoint((userinfo) -> userinfo.userService(customOauthUserService)) // 로그인 성공 이후 처리
                                .successHandler(authSuccessHandler) // JWT 발급 진행
                );
        return http.build();
    }
}
