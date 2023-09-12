package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.CustomUserDetails;
import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class PoderAdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final CPFValidator cpfValidator = new CPFValidator();
    public Usuario cadastrarUsuario(Usuario usuario) throws BusinessException {
        validarSenhasIguais(usuario.getSenha(), usuario.getConfirmaSenha());
        validarEmailUnico(usuario.getEmail());
        validarCpf(usuario.getCpf());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setConfirmaSenha(passwordEncoder.encode(usuario.getConfirmaSenha()));
        usuario.setStatus(Status.ATIVO);

        log.info("Usuário cadastrado com sucesso.");
        return usuarioRepository.save(usuario);
    }
    private boolean validarCpf(String cpf) throws BusinessException {
        try {
            cpfValidator.assertValid(cpf);
            System.out.println("CPF válido.");
            return true;

        } catch (InvalidStateException e) {
            throw new BusinessException("CPF inválido.");
        }
    }

    public void validarSenhasIguais(String senha, String confirmacaoSenha) throws BusinessException {
        if (!Objects.equals(senha, confirmacaoSenha)) {
            log.error("As senhas não coincidem entre si.");
            throw new BusinessException("Senhas não coincidem");
        }
    }

    void validarEmailUnico(String email) throws BusinessException {
        if (usuarioRepository.existsByEmail(email)) {
            log.error("Não é possível cadastrar usuário. O email já está em uso.");
            throw new BusinessException("O email já está em uso.");
        }
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario alternarStatusUsuario(int id) throws BusinessException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            if (usuario.getStatus().equals(Status.ATIVO)) {
                usuario.setStatus(Status.INATIVO);
                log.info("Usuário inativado com sucesso!");
            } else if (usuario.getStatus().equals(Status.INATIVO)) {
                usuario.setStatus(Status.ATIVO);
                log.info("Usuário reativado com sucesso!");
            }
            return usuarioRepository.save(usuario);
        }
        log.error("Usuário não encontrado!");
        throw new BusinessException("Usuário não encontrado!");
    }
    public Usuario alterarUsuario(Usuario usuarioAtualizado) throws BusinessException {
        Usuario usuarioCadastrado = usuarioRepository.findById(usuarioAtualizado.getId()).orElse(null);
        log.info("Usuário cadastrado: " + String.valueOf(usuarioCadastrado));
        log.info("Usuário atualizado: " + String.valueOf(usuarioAtualizado));

        usuarioCadastrado.setNome(usuarioAtualizado.getNome());

        if (validarCpf(usuarioAtualizado.getCpf())){
            usuarioCadastrado.setCpf(usuarioAtualizado.getCpf());
        }

        if (usuarioAtualizado.getSenha() != null && !passwordEncoder.matches(usuarioAtualizado.getSenha(), usuarioCadastrado.getSenha())) {
            usuarioCadastrado.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
            usuarioCadastrado.setConfirmaSenha(passwordEncoder.encode(usuarioAtualizado.getSenha())); // A senha é a mesma, então também deve ser criptografada
            log.info("Senha alterada com sucesso!");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails usuarioLogado = (CustomUserDetails) authentication.getPrincipal();

            if (!usuarioLogado.getUsername().equals(usuarioCadastrado.getEmail())) {
                usuarioCadastrado.setGrupo(usuarioAtualizado.getGrupo());
                usuarioRepository.save(usuarioCadastrado);
            } else {
                log.error("Acesso negado! Usuário não pode alterar o grupo do seu usuário");
                throw new BusinessException("Acesso negado! Não é possível alterar o grupo do seu usuário");
            }
        }
        return usuarioRepository.save(usuarioCadastrado);
    }

    public Usuario alterarSenha(int id, Usuario usuarioAtualizado) throws BusinessException {
        Usuario usuarioCadastrado = usuarioRepository.findById(id).orElse(null);
        log.info("SENHA CADASTRADO" + String.valueOf(usuarioCadastrado));

        if (!passwordEncoder.matches(usuarioAtualizado.getSenha(), usuarioCadastrado.getSenha())) {
            usuarioCadastrado.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
            usuarioCadastrado.setConfirmaSenha(passwordEncoder.encode(usuarioAtualizado.getConfirmaSenha()));
            log.info("SENHA ALTERADO 1" + String.valueOf(usuarioAtualizado));

            if (usuarioAtualizado.getSenha().equals(usuarioAtualizado.getConfirmaSenha())) {
                log.info("Senha alterada com sucesso!");
                return usuarioRepository.save(usuarioCadastrado);
            } else {
                log.error("Senhas não são iguais!");
                throw new BusinessException("As senhas não são iguais!");
            }
        } else {
            log.error("Não foi possível alterar, a senha não pode ser igual a anterior!");
            throw new BusinessException("A senha não pode ser igual a anterior!");
        }
    }

    public Usuario buscarUsuarioPorId(int id) throws BusinessException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        } else {
            throw new BusinessException("Usuário não encontrado com o ID: " + id);
        }
    }
}

