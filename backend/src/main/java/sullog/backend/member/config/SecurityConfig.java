package sullog.backend.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sullog.backend.member.config.jwt.JwtAccessDeniedHandler;
import sullog.backend.member.config.jwt.JwtAuthFilter;
import sullog.backend.member.config.jwt.JwtAuthenticationEntryPoint;
import sullog.backend.member.service.CustomOAuth2UserService;

@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(CustomOAuth2UserService oAuth2UserService, OAuth2SuccessHandler successHandler, JwtAuthFilter jwtAuthFilter, JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.oAuth2UserService = oAuth2UserService;
        this.successHandler = successHandler;
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable() // Rest API 서버이기때문에 CSRF 처리를 해제
                .formLogin().disable()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Custom Jwt 토큰 필터를 filter chain에 추가
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and()
                .authorizeRequests()
                    .antMatchers("/token/expired").permitAll() //token
                    .anyRequest().authenticated()
                    .and()
                .oauth2Login()
                    .successHandler(successHandler)
                    .loginPage("/token/expired")
                    .userInfoEndpoint().userService(oAuth2UserService)
                    .and()
                .and()
                .build();
    }
}
