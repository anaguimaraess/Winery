package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.admin.CadastroUsuarioService;
import br.com.ecommerce.winery.services.admin.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = cadastroUsuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

  /*@PutMapping("/alterar")
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioAtualizado = cadastroUsuarioService.atualizarUsuario(usuario);

        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
  * */

    @PutMapping("/inativar")
    public ResponseEntity<?> inativarUsuario(@RequestBody Usuario usuario) {
        try {
            Optional<Usuario> optionalUsuario = cadastroUsuarioService.buscarUsuarioPorId(usuario.getId());

            if (optionalUsuario.isPresent()) {
                Usuario usuarioAtual = optionalUsuario.get();

                if (usuarioAtual.getStatus() == Status.ATIVO) {
                    Usuario usuarioInativo = cadastroUsuarioService.inativarUsuario(usuarioAtual.getId());
                    log.info("Usuário inativado com sucesso!");
                    return ResponseEntity.status(HttpStatus.OK).body("Usuário inativado com sucesso!");
                } else {
                    log.error("Não foi possível inativar, pois o usuário já está inativo!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível inativar, pois o usuário já está inativo!");
                }
            } else {
                log.error("Usuário não encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/reativar")
    public ResponseEntity<?> reativarUsuario(@RequestBody Usuario usuario) {
        try {
            Optional<Usuario> optionalUsuario = cadastroUsuarioService.buscarUsuarioPorId(usuario.getId());

            if (optionalUsuario.isPresent()) {
                Usuario usuarioAtual = optionalUsuario.get();

                if (usuarioAtual.getStatus() == Status.INATIVO) {
                    Usuario usuarioReativado = cadastroUsuarioService.reativarUsuario(usuarioAtual.getId());
                    log.info("Usuário reativado com sucesso!");
                    return ResponseEntity.status(HttpStatus.OK).body("Usuário reativado com sucesso!");
                } else {
                    log.error("Não foi possível reativar, pois o usuário já está ativo!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível reativar, pois o usuário já está ativo!");
                }
            } else {
                log.error("Usuário não encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/filtro")
    public List<Usuario> filtroPorNome(@RequestParam("nome") String nome) {
        return this.usuarioRepository.findByNomeContains(nome);
    }
}
