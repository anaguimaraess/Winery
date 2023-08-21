package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.services.admin.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listarUsuarios")
    public List<Usuario> listarTodosUsuarios() {
        return usuarioService.listarTodosUsuarios();
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);

        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{id}/inativar")
    public ResponseEntity<?> inativarUsuario(@PathVariable int id) {

     try{
         Usuario usuarioInativo = usuarioService.inativarUsuario(id);
         return ResponseEntity.status(HttpStatus.OK).body("Usuário inativado com sucesso!");
     }catch(BusinessException e){
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
     }
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<?> reativarUsuario(@PathVariable int id) {
      try{
          Usuario usuarioAtivo = usuarioService.reativarUsuario(id);
          return ResponseEntity.status(HttpStatus.OK).body("Usuário ativado com sucesso!");
      }catch (BusinessException e){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    }


}
