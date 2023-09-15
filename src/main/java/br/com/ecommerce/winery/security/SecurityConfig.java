package br.com.ecommerce.winery.security;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.ecommerce.winery.security.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CPFValidator cpfValidator() {
        return new CPFValidator();
    }

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN") // Adicionado o prefixo 'ROLE_'
                .antMatchers("/usuario/**").hasAnyAuthority("ADMIN") // Configuração para o endpoint /usuario/**
                .antMatchers("/estoque/**").hasAnyAuthority("ESTOQUISTA", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/authentication/login")
                .failureUrl("/authentication/login?error=true")
                .successHandler(loginSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/authentication/logout")
                .logoutSuccessUrl("/authentication/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/styles/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/imagens/**");
    }

}
