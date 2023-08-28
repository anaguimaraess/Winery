package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.CadastroUsuarioService;
import br.com.ecommerce.winery.utils.MensagemRetorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;
    @Autowired
    private MensagemRetorno mensagemRetorno;

    @RequestMapping(value = "/cadastrar", method = RequestMethod.GET)
    public String getCadastroForm() {
        return "cadastroUsuario";
    }
    @PostMapping("/cadastrar")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model model, HttpServletResponse response) {
        try {
            Usuario novoUsuario = cadastroUsuarioService.cadastrarUsuario(usuario);
            mensagemRetorno.adicionarMensagem(model, "sucesso", "Usuário cadastrado com sucesso!");
            response.setStatus(HttpStatus.CREATED.value());
        } catch (BusinessException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mensagemRetorno.adicionarMensagem(model, "erro", "Erro ao cadastrar usuário: " + e.getMessage());
        }
        return "cadastroUsuario";
    }
}