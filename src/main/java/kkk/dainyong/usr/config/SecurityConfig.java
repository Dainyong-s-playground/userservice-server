package kkk.dainyong.usr.config;

import jakarta.servlet.http.HttpServletResponse;
import kkk.dainyong.usr.login.jwt.JWTFilter;
import kkk.dainyong.usr.login.jwt.JWTUtil;
import kkk.dainyong.usr.login.service.CustomOAuth2UserService;
import kkk.dainyong.usr.login.service.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JWTUtil jwtUtil;
    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class)
                .oauth2Login(oauth2Login -> oauth2Login

                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService))
                                .successHandler(customSuccessHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )

                .logout(logout ->
                        logout
                                .logoutUrl("/auth/logout") // 로그아웃 URL 설정
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.setStatus(HttpServletResponse.SC_OK); // HTTP 200 OK 설정
                                    response.getWriter().flush(); // 응답 바디 비우기
                                })
                                .deleteCookies("Authorization","JSESSIONID")

                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}
