package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.CustomUserDetails;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }
        return new CustomUserDetails(user);
    }
}
