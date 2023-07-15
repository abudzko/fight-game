package budzko.backend.starters.jwt;

import budzko.backend.starters.jwt.filter.CustomAccessDeniedHandler;
import budzko.backend.starters.jwt.filter.CustomAuthEntryPoint;
import budzko.backend.starters.jwt.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@ComponentScan
@RequiredArgsConstructor
public class JwtFilterConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    @Value("${security.jwt.validation.enabled1:true}")
    private boolean jwtValidationEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionFixation().none()
                .and()
                .addFilterAfter(jwtTokenFilter, SecurityContextHolderFilter.class)
                .cors();

        if (jwtValidationEnabled) {
            http.authorizeHttpRequests()
                    //TODO
                    .requestMatchers("/websocket/message/player").hasAuthority("PLAYER")
                    .and()
                    .exceptionHandling().authenticationEntryPoint(customAuthEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler);
        } else {
            http.authorizeHttpRequests()
                    .anyRequest().permitAll();
        }
        return http.build();
    }
}
