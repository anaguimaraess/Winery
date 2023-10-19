package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.backoffice.CustomUserDetails;
import br.com.ecommerce.winery.models.backoffice.Usuario;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.CustomClientDetails;
import br.com.ecommerce.winery.repositories.ClienteRepository;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario user = userRepository.findByEmail(email);
        Cliente cliente = clienteRepository.findByEmail(email);
        if (user != null) {
            return new CustomUserDetails(user);

        } else if (cliente != null) {
            return new CustomClientDetails(cliente);
        }
        throw new UsernameNotFoundException("Usuário não encontrado.");

    }
}
