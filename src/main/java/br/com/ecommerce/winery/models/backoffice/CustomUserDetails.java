package br.com.ecommerce.winery.models.backoffice;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails extends User {

    private final String nome;
    private final Grupo grupo;

    public CustomUserDetails(Usuario usuario) {
        super(usuario.getEmail(), usuario.getSenha(), getAuthority(usuario));
        this.nome = usuario.getNome();
        this.grupo = usuario.getGrupo();
    }

    public String getNome() {
        return nome;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    private static Collection<? extends GrantedAuthority> getAuthority(Usuario usuario) {
        return Collections.singletonList(new SimpleGrantedAuthority(usuario.getGrupo().toString()));
    }

}