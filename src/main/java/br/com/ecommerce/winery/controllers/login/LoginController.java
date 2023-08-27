package br.com.ecommerce.winery.controllers.login;


import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/authentication")
public class LoginController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> logar(@RequestBody Usuario usuario) {
        try {
            String email = usuario.getEmail();
            String senha = usuario.getSenha();
            boolean login = loginService.login(email, senha);
            return ResponseEntity.status(HttpStatus.OK).body(login);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> deslogar() {
        try {
            loginService.logout();
            return ResponseEntity.status(HttpStatus.OK).body("Usuário deslogado!");
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
