package br.com.ecommerce.winery.controllers.login;

import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
@RequestMapping(path = "")
public class LoginClienteController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginForm() {
        return "loginCliente";
    }

    @GetMapping("/logout")
    public String logoutSuccess() {
        return "loginCliente";
    }
}
