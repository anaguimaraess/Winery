package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Grupo;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class LoginService {
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

    public void logout() throws BusinessException {
        Usuario usuarioLogado = (Usuario) httpSession.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            throw new BusinessException("Nenhum usuário logado para desconectar.");
        }
        httpSession.removeAttribute("usuarioLogado");
        log.info("Usuário desconectado com sucesso!");
    }

    public boolean estaLogado() {
        boolean isLoggedIn = httpSession.getAttribute("usuarioLogado") != null;
        log.info("Usuário está logado: {}", isLoggedIn);
        return isLoggedIn;
    }

    private Usuario obterUsuarioLogado() throws BusinessException {
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
        return usuarioLogado.getGrupo() == Grupo.ADMIN;
    }

    public boolean ehEstoquista() throws BusinessException {
        Usuario usuarioLogado = obterUsuarioLogado();
        return usuarioLogado.getGrupo() == Grupo.ESTOQUISTA;
    }

    public boolean ehUsuario() throws BusinessException {
        Usuario usuarioLogado = obterUsuarioLogado();
        return usuarioLogado.getGrupo() == Grupo.USUARIO;
    }
}
