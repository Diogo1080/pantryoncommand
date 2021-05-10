package com.pantryoncommand.configuration;

import com.pantryoncommand.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
/**
 * Class for security configurations
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public SecurityConfig(UserAuthenticationEntryPoint userAuthenticationEntryPoint,
                          UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    /**
     * Override HttpSecurity configs
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                //.addFilterBefore(new CookieAuthFilter(userAuthenticationProvider), JwtAuthFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/ingredients/**", "/api/categories/**", "/api/recipes/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/auth/login", "/api/users").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public AuthorizationValidatorService authorized() {
        return new AuthorizationValidatorService();
    }

}
