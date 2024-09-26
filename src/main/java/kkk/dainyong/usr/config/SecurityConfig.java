package kkk.dainyong.usr.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kkk.dainyong.usr.login.jwt.JWTFilter;
import kkk.dainyong.usr.login.jwt.JWTUtil;
import kkk.dainyong.usr.login.service.CustomOAuth2UserService;
import kkk.dainyong.usr.login.service.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

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
//                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//
//                        CorsConfiguration configuration = new CorsConfiguration();
//
//                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
//                        configuration.setAllowedMethods(Collections.singletonList("*"));
//                        configuration.setAllowCredentials(true);
//                        configuration.setAllowedHeaders(Collections.singletonList("*"));
//                        configuration.setMaxAge(3600L);
//
//                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
//                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//
//                        return configuration;
//                    }
//                }))


//


                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class)

                .oauth2Login(oauth2Login -> oauth2Login

//                              .failureUrl("/login?error")
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService))
                                .successHandler(customSuccessHandler)
                )


                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )

//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/login")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                )
                .logout(logout ->
                        logout
                                .logoutUrl("/auth/logout") // 로그아웃 URL 설정
                                .logoutSuccessUrl("/login")
                                .deleteCookies("Authorization","JSESSIONID")
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }


//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
}
