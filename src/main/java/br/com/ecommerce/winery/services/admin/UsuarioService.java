package br.com.ecommerce.winery.services.admin;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarPorNome(String nome){
        return usuarioRepository.findByNome(nome);
    }

    public Usuario atualizarUsuario(int id, Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioCadastrado = usuarioRepository.findById(id);

        if (usuarioCadastrado.isPresent()) {
            Usuario usuario = usuarioCadastrado.get();
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setSenha(usuarioAtualizado.getSenha());
            usuario.setGrupo(usuarioAtualizado.getGrupo());

            return usuarioRepository.save(usuario);
        } else {
            return null;
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


}
