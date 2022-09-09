package com.chuwa.redbook.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author b1go
 * @date 7/1/22 3:46 PM
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityInmemoryConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    /**
     * In memory auth
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails chuwa = User.builder().username("chuwa").password(
                passwordEncoder().encode("chuwa")).roles("USER").build();
        UserDetails zhangsan = User.builder().username("admin").password(
                passwordEncoder().encode("admin")).roles("ADMIN").build();
        return new InMemoryUserDetailsManager(chuwa, zhangsan);
    }
}
