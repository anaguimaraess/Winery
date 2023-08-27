package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Grupo;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
@Slf4j
public class LoginService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private HttpSession httpSession;

    public boolean login(String email, String senha) throws BusinessException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null && passwordEncoder.matches(senha, usuario.getSenha())) {
            httpSession.setAttribute("usuarioLogado", usuario);
            log.info("Usuário logado com sucesso!");
            return true;
        }
        log.error("Login inválido!");
        throw new BusinessException("Usuário não encontrado ou senha inválida.");
    }

    public void logout() {
        httpSession.removeAttribute("usuarioLogado");
        log.info("Usuário desconectado com sucesso!");
    }

    public boolean estaLogado() {
        boolean isLoggedIn = httpSession.getAttribute("usuarioLogado") != null;
        log.info("Usuário está logado: {}", isLoggedIn);
        return isLoggedIn;
    }

    public Usuario obterUsuarioLogado() throws BusinessException {
        Usuario loggedUser = (Usuario) httpSession.getAttribute("usuarioLogado");
        if (loggedUser == null) {
            log.error("Nenhum usuário logado.");
            throw new BusinessException("Nenhum usuário logado.");
        }
        log.info("Usuário logado: {}", loggedUser.getNome());
        return loggedUser;
    }

    public boolean ehAdmin() throws BusinessException {
        Usuario usuarioLogado = obterUsuarioLogado();
        return usuarioLogado != null && usuarioLogado.getGrupo() == Grupo.ADMIN;
    }

    public boolean ehEstoquista() throws BusinessException {
        Usuario usuarioLogado = obterUsuarioLogado();
        return usuarioLogado != null && usuarioLogado.getGrupo() == Grupo.ESTOQUISTA;
    }

    public boolean ehUsuario() throws BusinessException {
        Usuario usuarioLogado = obterUsuarioLogado();
        return usuarioLogado != null && usuarioLogado.getGrupo() == Grupo.USUARIO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }
        return new User(usuario.getEmail(), usuario.getSenha(), Collections.emptyList());
    }
}
