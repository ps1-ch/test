package com.github.ps1ch.test.testserver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private val environment: Environment? = null

    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Bean
    fun encoder() = BCryptPasswordEncoder()

    override fun configure(auth: AuthenticationManagerBuilder) {
        // @formatter:off
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(encoder())
        // @formatter:on
    }

    override fun configure(web: WebSecurity) {

        val profiles = environment?.activeProfiles
        if (profiles != null && profiles.contains("dev")) {
            // @formatter:off
            web
                .ignoring()
                .antMatchers("/h2-console/**")
            // @formatter:on
        }
    }

    override fun configure(http: HttpSecurity) {
        // @formatter:off
        http
            .authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .formLogin()
            .and()
                .httpBasic()
            .and()
                .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        // @formatter:on
    }
}
