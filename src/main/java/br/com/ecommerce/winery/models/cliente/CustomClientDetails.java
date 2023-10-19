package br.com.ecommerce.winery.models.cliente;

import br.com.ecommerce.winery.models.backoffice.Grupo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

public class CustomClientDetails extends User {

    private final String nome;
    private final Grupo grupo;

    public CustomClientDetails(Cliente usuario) {
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

    private static Collection<? extends GrantedAuthority> getAuthority(Cliente usuario) {
        return Collections.singletonList(new SimpleGrantedAuthority(usuario.getGrupo().toString()));
    }

}