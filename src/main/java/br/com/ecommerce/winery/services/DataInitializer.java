package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.backoffice.Usuario;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static br.com.ecommerce.winery.models.backoffice.Grupo.ADMIN;
import static br.com.ecommerce.winery.models.Status.ATIVO;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail("admin@winery.com") == null) {
            Usuario admin = new Usuario();
            admin.setNome("Admin User");
            admin.setEmail("admin@winery.com");
            admin.setCpf("58213161033");
            admin.setSenha(passwordEncoder.encode("2023"));
            admin.setConfirmaSenha(admin.getSenha());
            admin.setGrupo(ADMIN);
            admin.setStatus(ATIVO);
            usuarioRepository.save(admin);
        }
    }
}