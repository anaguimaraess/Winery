package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.backoffice.Grupo;
import br.com.ecommerce.winery.models.backoffice.Usuario;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Suponha que o seu usuário tenha um campo 'grupo' que represente o grupo ao qual ele pertence.
        // Você pode usar esse campo para atribuir a função correspondente ao usuário.
        Grupo grupo = user.getGrupo();

        // Converter o enum Grupo em uma autoridade (role) com o prefixo 'ROLE_'.
        authorities.add(new SimpleGrantedAuthority(grupo.name()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getSenha(),
                authorities
        );
    }
}