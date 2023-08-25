package br.com.ecommerce.winery.services.admin;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CadastroUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(Usuario usuario) throws BusinessException {
        validarSenhasIguais(usuario.getSenha(), usuario.getConfirmaSenha());
        validarEmailUnico(usuario.getEmail());

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setConfirmaSenha(passwordEncoder.encode(usuario.getConfirmaSenha()));
        usuario.setStatus(Status.ATIVO);

        return usuarioRepository.save(usuario);
    }


    public void validarSenhasIguais(String senha, String confirmacaoSenha) throws BusinessException {
        if (!Objects.equals(senha, confirmacaoSenha)) {
            log.error("As senhas não coincidem entre si.");
            throw new BusinessException("Senhas não coincidem");
        }
    }

    private void validarEmailUnico(String email) throws BusinessException {
        if (usuarioRepository.existsByEmail(email)) {
            log.error("Não é possível cadastrar usuário. O email já está em uso.");
            throw new BusinessException("O email já está em uso.");
        }
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario inativarUsuario(int id) throws BusinessException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (!usuario.getStatus().equals(Status.INATIVO)) {
            usuario.setStatus(Status.INATIVO);
            log.info("Usuário inativado com sucesso!");
            return usuarioRepository.save(usuario);
        }
        log.error("Usuário já está inativo!");
        throw new BusinessException("Não é possível inativar, usuário ja está inativo!");
    }

    public Usuario reativarUsuario(int id) throws BusinessException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (!usuario.getStatus().equals(Status.ATIVO)) {
            usuario.setStatus(Status.ATIVO);
            log.info("Usuário reativado com sucesso!");
            return usuarioRepository.save(usuario);
        }
        log.error("Usuário já está ativo!");
        throw new BusinessException("Não é possível reativar, usuário já está ativo!");
    }
}
