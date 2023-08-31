package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Grupo;
import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CadastroUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(Usuario usuario) throws BusinessException {
        if (!loginService.ehAdmin()) {
            log.error("Você não é administrador. Não foi possível cadastrar usuário.");
            throw new BusinessException("Tentativa falha de cadastro de usuário.");
        }
        validarSenhasIguais(usuario.getSenha(), usuario.getConfirmaSenha());
        validarEmailUnico(usuario.getEmail());

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setConfirmaSenha(passwordEncoder.encode(usuario.getConfirmaSenha()));
        usuario.setStatus(Status.ATIVO);

        log.info("Usuário cadastrado com sucesso.");
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



    public Usuario alterarNomeUsuario(int id, Usuario usuarioAtualizado) throws BusinessException {
        Usuario usuarioCadastrado = buscarUsuarioPorId(id);
        System.out.println(usuarioCadastrado.getNome());

        if (!usuarioCadastrado.getNome().equals(usuarioAtualizado.getNome())) {
            usuarioCadastrado.setNome(usuarioAtualizado.getNome());
            log.info("Nome de usuário alterado com sucesso!");
            return usuarioRepository.save(usuarioCadastrado);
        } else {
            log.error("Inserir nome diferente do anterior!");
            throw new BusinessException("Nome não pode ser igual ao anterior!");
        }
    }

    public Usuario alterarCpfUsuario(int id, Usuario usuarioAtualizado) throws BusinessException {
        Usuario usuarioCadastrado = usuarioRepository.findById(id).orElse(null);

        if (!usuarioCadastrado.getCpf().equals(usuarioAtualizado.getCpf())) {
            usuarioCadastrado.setCpf(usuarioAtualizado.getCpf());
            log.info("CPF alterado com sucesso!");
            return usuarioRepository.save(usuarioCadastrado);
        }
        log.error("Inserir número de CPF diferente do anterior!");
        throw new BusinessException("CPF não pode ser igual ao anterior!");
    }

    public Usuario alterarSenha(int id, Usuario usuarioAtualiado) throws BusinessException {
        Usuario usuarioCadastrado = usuarioRepository.findById(id).orElse(null);

        if (!usuarioCadastrado.getSenha().equals(usuarioAtualiado.getSenha())) {
            usuarioCadastrado.setSenha(passwordEncoder.encode(usuarioAtualiado.getSenha()));
            usuarioCadastrado.setConfirmaSenha(passwordEncoder.encode(usuarioAtualiado.getConfirmaSenha()));
            if (usuarioAtualiado.getSenha().equals(usuarioAtualiado.getConfirmaSenha())) {
                log.info("Senha alterada com sucesso!");
            } else {
                log.error("Senhas não são iguais!");
                throw new BusinessException("As senhas não são iguais!");
            }
        } else {
            log.error("Não foi possível alterar, a senha não pode ser igual a anterior!");
            throw new BusinessException("A senha não pode ser igual a anterior!");
        }
        return usuarioRepository.save(usuarioCadastrado);
    }

    public Usuario alterarGrupo(int id, Usuario usuarioAtualizado) throws BusinessException{
        Usuario usuarioCadastrado = usuarioRepository.findById(id).orElse(null);
        Usuario usuarioLogado = loginService.obterUsuarioLogado();

        if (!usuarioLogado.getGrupo().equals(Grupo.ADMIN) && usuarioLogado.getId() == usuarioCadastrado.getId()) {
            log.error("Acesso negado! Usuário não pode alterar o grupo");
            throw new BusinessException("Acesso negado! Não é possível alterar seu próprio grupo");
        }else{
            usuarioCadastrado.setGrupo(usuarioAtualizado.getGrupo());
            log.info("Grupo atualizado com sucesso!");
        }
        return usuarioRepository.save(usuarioCadastrado);
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

