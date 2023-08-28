package br.com.ecommerce.winery.controllers.login;

import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/authentication")
public class LoginController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> logar(@RequestParam String email, @RequestParam String senha) {
        HttpHeaders headers = new HttpHeaders();
        try {
            boolean login = loginService.login(email, senha);
            headers.add("Location", "/admin/cadastrar");
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (BusinessException e) {
            headers.add("Location", "/authentication/login?error=true");
            return ResponseEntity.status(HttpStatus.FOUND)
                    .headers(headers)
                    .body(e.getMessage());
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
