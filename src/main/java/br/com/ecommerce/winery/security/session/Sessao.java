package br.com.ecommerce.winery.security.session;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class Sessao {

    public boolean isSessaoAtiva() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Sessão ativa para o usuário: " + authentication.getName());
            return true;
        } else {
            System.out.println("Sessão não ativa");
            return false;
    }
}}
