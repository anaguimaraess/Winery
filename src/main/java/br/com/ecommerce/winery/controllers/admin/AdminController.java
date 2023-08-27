package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @PutMapping("/alterar")
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario) {
      try{
          Usuario usuarioAtualizado = cadastroUsuarioService.alterarUsuario(usuario);
          return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);

      }catch (BusinessException e){
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
    }


    @GetMapping("/listarUsuarios")
    public List<Usuario> listarTodosUsuarios() {
        return cadastroUsuarioService.listarTodosUsuarios();
    }

    @PutMapping("/inativar")
    public ResponseEntity<?> inativarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioAtivo = cadastroUsuarioService.inativarUsuario(usuario.getId());
            return ResponseEntity.status(HttpStatus.OK).body(usuarioAtivo);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping("/reativar")
    public ResponseEntity<?> reativarUsuario(@RequestBody Usuario usuario) {
      try{
          Usuario usuarioInativo = cadastroUsuarioService.reativarUsuario(usuario.getId());
          return ResponseEntity.status(HttpStatus.OK).body(usuarioInativo);
      }catch (BusinessException e){
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
    }

    @GetMapping("/filtro")
    public List<Usuario> filtroPorNome(@RequestParam("nome") String nome) {
        return this.usuarioRepository.findByNomeContains(nome);
    }
}
