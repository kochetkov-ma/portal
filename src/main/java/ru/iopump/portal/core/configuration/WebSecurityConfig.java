package ru.iopump.portal.core.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * Общая конфигурация Spring Security. Авторизация через пользователей из БД.
 * Сессии в БД.
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    private static final String ENCODED_PASS = "$2a$11$MVep13x4Q5AVki5/ZaOy9uo9FNtc7ZTpo.Zo5IV6BG.Ar/TXq6iTK";
    private final UserDetailsService portalUserDetailsService;

    @Autowired
    public WebSecurityConfig(final UserDetailsService portalUserDetailsService) {
        this.portalUserDetailsService = portalUserDetailsService;
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return portalUserDetailsService;
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    /**
     * Провайдер из DB.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("[MESSAGE] Dev password hash : '{}'", ENCODED_PASS);
        auth.userDetailsService(portalUserDetailsService).passwordEncoder(new BCryptPasswordEncoder(11));
    }

    /**
     * Secure the endpoints with HTTP Basic authentication.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                    .exceptionHandling()
                .and()
                    .formLogin()
                    .successHandler(new SimpleUrlAuthenticationSuccessHandler())
                    .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/auth/session").permitAll()
                        .antMatchers(HttpMethod.GET,"/auth/**").authenticated()
                        .antMatchers(HttpMethod.DELETE,"/auth/**").authenticated()
                        .antMatchers("/jira/**").hasAnyRole(USER_ROLE, ADMIN_ROLE)
                        .antMatchers("/admin/**").hasAnyRole(ADMIN_ROLE)
                        .antMatchers("/h2/**").permitAll()
                .and()
                    .httpBasic()
                .and()
                    .requestCache()
                    .requestCache(new NullRequestCache());
        // @formatter:on
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}