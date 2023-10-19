package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.CustomClientDetails;
import br.com.ecommerce.winery.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomClientDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente user = clienteRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("E-mail e/ou senha não encontrados.");
        }
        return new CustomClientDetails(user);
    }
}
