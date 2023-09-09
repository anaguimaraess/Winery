package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.Imagem;
import br.com.ecommerce.winery.models.Produto;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.ImagemRepository;
import br.com.ecommerce.winery.repositories.ProdutoRepository;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.PoderAdminService;
import br.com.ecommerce.winery.utils.MensagemRetorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private PoderAdminService cadastroProdutoService;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ImagemRepository imagemRepository;
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
    public String listarTodosUsuarios(Model model, HttpServletResponse response) {
        List<Usuario> usuarios = cadastroUsuarioService.listarTodosUsuarios();
        model.addAttribute("usuarios", usuarios);
        response.setStatus(HttpStatus.OK.value());
        return "listaUsuario";
    }

    @RequestMapping(value = "/alterarStatus", method = {RequestMethod.GET, RequestMethod.POST})
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


    @GetMapping("/filtro")
    public String filtroPorNome(@RequestParam("nome") String nome, Model model, HttpServletResponse response) {
        nome = nome.toLowerCase(); // Converter o nome para letras minúsculas
        List<Usuario> usuarios = this.usuarioRepository.findByNomeContains(nome);
        model.addAttribute("usuarios", usuarios);
        response.setStatus(HttpStatus.OK.value());
        return "listaUsuario";
    }


    @GetMapping("/filtroNomeProduto")
    public String filtroNomeDoProduto(@RequestParam("nomeProduto") String nomeProduto, Model model, HttpServletResponse response) {
        nomeProduto = nomeProduto.toLowerCase();
        List<Produto> produtos = this.produtoRepository.findByNomeProdutoContainingIgnoreCase(nomeProduto);
        model.addAttribute("produtos", produtos);
        response.setStatus(HttpStatus.OK.value());
        return "listaProdutos";
    }


    @GetMapping("/listarProdutosDecrescente")
    public String listarTodosOsProdutosDecrescente(Model model) {

        List<Produto> produtos = cadastroProdutoService.listarTodosProdutosDecrescente();
        model.addAttribute("produtos", produtos);
        return "listaProdutosDecrescente";
    }

    @GetMapping("/listarProdutos")
    public String listarTodosOsProdutos(Model model) {

        List<Produto> produtos = cadastroProdutoService.listarTodosProdutos();
        model.addAttribute("produtos", produtos);
        return "listaProdutos";
    }

    @RequestMapping(value = "/alterarStatusProduto", method = {RequestMethod.GET, RequestMethod.POST})
    public String alterarStatusDoProduto(@RequestParam("idProduto") int idProduto, Model model, HttpServletResponse response) {
        try {
            Produto produtoAlterado = cadastroProdutoService.alterarStatusDoProduto(idProduto);
            List<Produto> produtos = cadastroProdutoService.listarTodosProdutosDecrescente();
            response.setStatus(HttpStatus.OK.value());
            model.addAttribute("produtos", produtos);
            return "redirect:/admin/listarProdutos";
        } catch (BusinessException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mensagemRetorno.adicionarMensagem(model, "erro", "Erro ao alterar status do produto: " + e.getMessage());
            return "listaProdutosDecrescente";
        }
    }

    @PostMapping("/cadastrarProdutos")
    String cadastrarProdutosComImagens(@ModelAttribute("produto") Produto produto, @RequestParam("imagem") MultipartFile[] imagens,
                                       @RequestParam("imagemPrincipal") String imagemPrincipal, Model model, HttpServletResponse response) {
        try {

            Produto produtoCadastrado = cadastroProdutoService.cadastrarProdutos(produto);

            for (MultipartFile imagem : imagens) {
                cadastroProdutoService.salvarImagens(imagem, produtoCadastrado);
            }
            mensagemRetorno.adicionarMensagem(model, "sucesso", "Produto cadastrado com sucesso!");
            response.setStatus(HttpStatus.CREATED.value());

        } catch (BusinessException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mensagemRetorno.adicionarMensagem(model, "erro", "Erro ao cadastrar o produto: " + e.getMessage());
        }
        return "listarProdutos";
    }

}