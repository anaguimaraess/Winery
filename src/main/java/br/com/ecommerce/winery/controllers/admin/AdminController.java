package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.produtos.Produto;
import br.com.ecommerce.winery.models.backoffice.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.ProdutoRepository;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.AdminService;
import br.com.ecommerce.winery.utils.MensagemRetorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AdminService cadastroUsuarioService;
    @Autowired
    private AdminService cadastroProdutoService;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MensagemRetorno mensagemRetorno;
    @GetMapping("/home")
    public String homeBackOffice() {
        return "homeBackOffice";
    }

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
            return ResponseEntity.ok("Sucesso:Usuário cadastrado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro:" + e.getMessage());
        }
    }
    @PostMapping("/alterar")
    public ResponseEntity<String> alterarDadosUsuario(@ModelAttribute("usuario") Usuario usuario) {
        try {
            Usuario usuarioAtualizado = cadastroUsuarioService.alterarUsuario(usuario);
            return ResponseEntity.ok("Sucesso: Usuário alterado com sucesso! ");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
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


    //   ---------------> CONTROLLER DE PRODUTO <------------------


    @GetMapping("/listarProdutos")
    public String listarTodosOsProdutos(Model model, @RequestParam(defaultValue = "0") int page, HttpServletResponse response) throws BusinessException {
        Pageable pageable = PageRequest.of(page, 10); // 10 produtos por página
        Page<Produto> produtosPage = cadastroProdutoService.listarTodosProdutosDecrescente(pageable);
        model.addAttribute("produtos", produtosPage); // Adicione a página ao modelo, não apenas a lista
        response.setStatus(HttpStatus.OK.value());
        return "listaProduto";
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

    @GetMapping("/exibirProduto")
    public String exibirProduto(@RequestParam("id") int id, Model model, HttpServletResponse response) {
        try {
            Produto produto = cadastroProdutoService.buscarProdutoPorId(id);
            model.addAttribute("produto", produto);
            response.setStatus(HttpStatus.OK.value());
            return "exibirProduto";
        } catch (BusinessException e) {
            return "erro";
        }
    }


    @PostMapping("/cadastrarProdutos")
    @Transactional
    public ResponseEntity<String> cadastrarProduto(
            @ModelAttribute("produto") Produto produto,
            @RequestParam("imagem") MultipartFile[] imagens,
            @RequestParam("imgPrincipal") int imgPrincipal,
            RedirectAttributes redirect
    ) {
        try {
            Produto produtoCadastrado = cadastroProdutoService.cadastrarProdutos(produto, imagens, imgPrincipal);
            return ResponseEntity.ok("Sucesso: Produto cadastrado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro:" + e.getMessage());
        }
    }

    @GetMapping("/filtroNomeProduto")
    public String filtroNomeDoProduto(@RequestParam("nomeProduto") String nomeProduto, Model model, HttpServletResponse response) {
        Pageable pageable = PageRequest.of(0, 10); // Escolha a página atual e o tamanho da página como desejar
        Page<Produto> produtosFiltrados = this.produtoRepository.findByNomeProdutoContainingIgnoreCaseOrderByIdProdutoDesc(nomeProduto, pageable);
        model.addAttribute("produtos", produtosFiltrados);
        response.setStatus(HttpStatus.OK.value());
        return "listaProduto";
    }

    @RequestMapping(value = "/ativarProduto", method = {RequestMethod.GET, RequestMethod.POST})
    public String ativarProduto(@RequestParam("idProduto") int idProduto,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                Model model,
                                HttpServletResponse response) {
        try {
            Produto produtoAlterado = cadastroProdutoService.ativarProduto(idProduto);

            response.setStatus(HttpStatus.OK.value());
            return "redirect:/admin/listarProdutos?page=" + page;
        } catch (BusinessException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mensagemRetorno.adicionarMensagem(model, "erro", "Erro ao ativar o produto: " + e.getMessage());
            return "listaProduto";
        }
    }

    @RequestMapping(value = "/inativarProduto", method = {RequestMethod.GET, RequestMethod.POST})
    public String inativarProduto(@RequestParam("idProduto") int idProduto,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  Model model,
                                  HttpServletResponse response) {
        try {
            Produto produtoAlterado = cadastroProdutoService.inativarProduto(idProduto);

            response.setStatus(HttpStatus.OK.value());
            return "redirect:/admin/listarProdutos?page=" + page;
        } catch (BusinessException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mensagemRetorno.adicionarMensagem(model, "erro", "Erro ao inativar o produto: " + e.getMessage());
            return "listaProduto";
        }


    }

    @PostMapping("/alterarProduto")
    public String editarProduto(@ModelAttribute Produto produto,
                                @RequestParam(required = false) MultipartFile[] imagemInput,
                                @RequestParam("imagensParaRemover") String imagensParaRemover,
                                @RequestParam("imagenPrinclAt") String imagensParaAtualizar,
                                @RequestParam(required = false) String imgPrincipal, RedirectAttributes redirect, Model model, HttpServletResponse response) {
        try {
            cadastroProdutoService.editarProduto(produto, imagemInput, imagensParaRemover, imagensParaAtualizar, imgPrincipal, redirect);
            model.addAttribute("produto", produto);
            response.setStatus(HttpStatus.OK.value());
            // return ResponseEntity.ok("Sucesso: Produto alterado com sucesso!");
           return "redirect:/admin/listarProdutos";
        } catch (Exception e) {
            return "redirect:/admin/listarProdutos";
        }
    }
    @GetMapping("/buscarProduto")
    public String buscarProdutoParaAlterar(@RequestParam(name = "id", required = false) int id, Model model, HttpServletResponse response) {
        Optional<Produto> produto = produtoRepository.findById(id);
        model.addAttribute("produto", produto.get());
        response.setStatus(HttpStatus.OK.value());
        return "alterarProduto";
    }

}