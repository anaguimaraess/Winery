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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/alterarNome")
    public ResponseEntity<?> atualizarNomeDoUsuario(@RequestBody Usuario usuario) {
        try {
            int usuarioId = usuario.getId();
            Usuario usuarioAtualizado = cadastroUsuarioService.buscarUsuarioPorId(usuarioId);

            if (usuario.getNome() != null) {
                usuarioAtualizado = cadastroUsuarioService.alterarNomeUsuario(usuario.getId(), usuario);
                return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
            }
            return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/alterarCpf")
    public ResponseEntity<?> atualizarCpfDoUsuario(@RequestBody Usuario usuario) {
        try {
            int usuarioId = usuario.getId();
            Usuario usuarioAtualizado = cadastroUsuarioService.buscarUsuarioPorId(usuarioId);

            if (usuario.getCpf() != null) {
                usuarioAtualizado = cadastroUsuarioService.alterarCpfUsuario(usuario.getId(), usuario);
                return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
            }
            return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);

        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/alterarSenha")
    public ResponseEntity<?> atualizarSenhaDoUsuario(@RequestBody Usuario usuario) {
        try {
            int usuarioId = usuario.getId();
            Usuario usuarioAtualizado = cadastroUsuarioService.buscarUsuarioPorId(usuarioId);

            if (usuario.getSenha() != null) {
                usuarioAtualizado = cadastroUsuarioService.alterarSenha(usuario.getId(), usuario);
                return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
            }
            return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/alterarGrupo")
    public ResponseEntity<?> atualizarGrupoDoUsuario(@RequestBody Usuario usuario) {
        try {
            int usuarioId = usuario.getId();
            Usuario usuarioAtualizado = cadastroUsuarioService.buscarUsuarioPorId(usuarioId);

            if (usuario.getGrupo() != null) {
                usuarioAtualizado = cadastroUsuarioService.alterarGrupo(usuario.getId(), usuario);
                return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
            }
            return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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

    @GetMapping("/listar")
    public String listarTodosUsuarios(Model model) {
        List<Usuario> usuarios = cadastroUsuarioService.listarTodosUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "listaUsuario";
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
        try {
            Usuario usuarioInativo = cadastroUsuarioService.reativarUsuario(usuario.getId());
            return ResponseEntity.status(HttpStatus.OK).body(usuarioInativo);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/filtro")
    public List<Usuario> filtroPorNome(@RequestParam("nome") String nome) {
        return this.usuarioRepository.findByNomeContains(nome);
    }
}