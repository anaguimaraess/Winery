package br.com.ecommerce.winery.services.admin;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Usuario inativarUsuario(int id) throws BusinessException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        usuario.setStatus(Status.INATIVO);
        return usuarioRepository.save(usuario);
    }

    public Usuario reativarUsuario(int id) throws BusinessException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        usuario.setStatus(Status.ATIVO);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorId(int id) throws BusinessException {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            log.error("Não foi possível encontrar o usuário.");
            throw new BusinessException("Usuário não encontrado.");
        }
        return usuario;
    }

}
