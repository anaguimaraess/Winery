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
                .antMatchers("/admin/cadastrar").permitAll() // Permite acesso não autenticado ao endpoint de cadastro
                .antMatchers("/h2-console/**").permitAll() // Permite acesso ao console do H2
                .antMatchers("/admin/listarUsuariosPorNome").permitAll()
                .antMatchers("/admin/listarUsuarios").permitAll()
                .antMatchers("/admin/alterar").permitAll()
                .antMatchers("/admin/inativar").permitAll()
                .antMatchers("/admin/reativar").permitAll()
                .antMatchers("/admin/filtro").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin(); // Permite o uso do frame do H2 no mesmo domínio
    }

}


