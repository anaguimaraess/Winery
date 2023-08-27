package br.com.ecommerce.winery.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll() // Permitir acesso a todas as URLs
                .and()
                .csrf().disable() // Desativar proteção CSRF
                .headers().frameOptions().sameOrigin(); // Permitir o acesso ao H2 Console
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/admin/**").hasAuthority("ADMIN")
//                .antMatchers("/user/**").hasAuthority("USUARIO")
//                .antMatchers("/stockist/**").hasAuthority("ESTOQUISTA")
//                .antMatchers("/h2-console/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/index")
//                .defaultSuccessUrl("/home")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll()
//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/acesso-negado");
//
//        http.csrf().disable();
//        http.headers().frameOptions().sameOrigin();
//    }

}
