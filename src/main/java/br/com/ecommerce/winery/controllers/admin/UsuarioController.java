package br.com.ecommerce.winery.controllers.admin;
import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.services.admin.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable int id, @RequestBody Usuario usuario){
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);

        if(usuarioAtualizado != null){
            return ResponseEntity.ok(usuarioAtualizado);
        }else{
            return ResponseEntity.notFound().build();
        }

    }


}
