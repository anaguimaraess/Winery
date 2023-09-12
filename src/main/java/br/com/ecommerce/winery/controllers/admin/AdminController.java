package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.PoderAdminService;
import br.com.ecommerce.winery.utils.MensagemRetorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PoderAdminService cadastroUsuarioService;
    @Autowired
    private MensagemRetorno mensagemRetorno;

    @GetMapping("/listar")
    public String listarTodosUsuarios(Model model, HttpServletResponse response) {
        List<Usuario> usuarios = cadastroUsuarioService.listarTodosUsuarios();
        model.addAttribute("usuarios", usuarios);
        response.setStatus(HttpStatus.OK.value());
        return "listaUsuario";
    }
    @GetMapping("/filtro")
    public String filtroPorNome(@RequestParam("nome") String nome, Model model, HttpServletResponse response) {
        nome = nome.toLowerCase(); // Converter o nome para letras minúsculas
        List<Usuario> usuarios = this.usuarioRepository.findByNomeContains(nome);
        model.addAttribute("usuarios", usuarios);
        response.setStatus(HttpStatus.OK.value());
        return "listaUsuario";
    }
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(@ModelAttribute Usuario usuario, HttpServletResponse response) {
        try {
            Usuario novoUsuario = cadastroUsuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
    @PostMapping("/alterar")
    public ResponseEntity<String> alterarDadosUsuario(@ModelAttribute("usuario") Usuario usuario) {
        try {
            Usuario usuarioAtualizado = cadastroUsuarioService.alterarUsuario( usuario);
            return ResponseEntity.ok("Usuário alterado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/listarUsuarioPorId")
    public ResponseEntity<Usuario> listarPorId(@RequestBody Map<String, Integer> requestBody) {
        int id = requestBody.get("id");

        try {
            Usuario usuario = cadastroUsuarioService.buscarUsuarioPorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/alterarStatus", method = { RequestMethod.GET, RequestMethod.POST })
    public String alternarStatusUsuario(@RequestParam("id") int id, Model model, HttpServletResponse response) {
        try {
            Usuario usuarioAlterado = cadastroUsuarioService.alternarStatusUsuario(id);
            List<Usuario> usuarios = cadastroUsuarioService.listarTodosUsuarios();
            response.setStatus(HttpStatus.OK.value());
            model.addAttribute("usuarios", usuarios);
            return "redirect:/admin/listar";
        } catch (BusinessException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mensagemRetorno.adicionarMensagem(model, "erro", "Erro ao alterar status do usuário: " + e.getMessage());
            return "listaUsuario";
        }
    }
}