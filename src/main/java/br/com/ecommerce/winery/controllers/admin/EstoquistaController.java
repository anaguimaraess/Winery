package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.pedido.Pedido;
import br.com.ecommerce.winery.models.pedido.StatusPedido;
import br.com.ecommerce.winery.models.produtos.Produto;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.PedidoRepository;
import br.com.ecommerce.winery.repositories.ProdutoRepository;
import br.com.ecommerce.winery.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/estoque")
public class EstoquistaController {

    @Autowired
    private AdminService cadastroProdutoService;

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/homeEstoque")
    public String homeBackOffice() {
        return "homeBackOfficeEstoquista";
    }

    @GetMapping("/filtroNomeProduto")
    public String filtroNomeDoProduto(@RequestParam("nomeProduto") String nomeProduto, Model model, HttpServletResponse response) {
        Pageable pageable = PageRequest.of(0, 10); // Escolha a página atual e o tamanho da página como desejar
        Page<Produto> produtosFiltrados = this.produtoRepository.findByNomeProdutoContainingIgnoreCaseOrderByIdProdutoDesc(nomeProduto, pageable);
        model.addAttribute("produtos", produtosFiltrados);
        response.setStatus(HttpStatus.OK.value());
        return "listaProdutoEstoquista";
    }

    @GetMapping("/listarProdutos")
    public String listarTodosOsProdutos(Model model, @RequestParam(defaultValue = "0") int page, HttpServletResponse response) throws BusinessException {
        Pageable pageable = PageRequest.of(page, 10); // 10 produtos por página
        Page<Produto> produtosPage = cadastroProdutoService.listarTodosProdutosDecrescente(pageable);
        model.addAttribute("produtos", produtosPage); // Adicione a página ao modelo, não apenas a lista
        response.setStatus(HttpStatus.OK.value());
        return "listaProdutoEstoquista";
    }

    @GetMapping("/buscarProduto")
    public String buscarProdutoParaAlterar(@RequestParam(name = "id", required = false) int id, Model model, HttpServletResponse response) {
        Optional<Produto> produto = produtoRepository.findById(id);
        model.addAttribute("produto", produto.get());
        response.setStatus(HttpStatus.OK.value());
        return "alterarProdutoEstoquista";
    }

    @PostMapping("/alterarProduto")
    public String editarProduto(@ModelAttribute Produto produto,
                                @RequestParam(required = false) MultipartFile[] imagemInput,
                                @RequestParam("imagensParaRemover") String imagensParaRemover,
                                @RequestParam("imagemPrincipalNova") String imagensParaAtualizar,
                                @RequestParam(required = false) String imgPrincipal, RedirectAttributes redirect, Model model, HttpServletResponse response) {
        try {
            cadastroProdutoService.editarProduto(produto, imagemInput, imagensParaRemover, imagensParaAtualizar, imgPrincipal, redirect);
            model.addAttribute("produto", produto);
            response.setStatus(HttpStatus.OK.value());
            return "redirect:/estoque/listarProdutos";
        } catch (Exception e) {
            return "redirect:/estoque/listarProdutos";
        }
    }

    @PostMapping("/atualizarStatusPedido")
    public String atualizarStatus(@RequestParam int id, @RequestParam StatusPedido stausPedido, HttpServletResponse response) {
        try {
            Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);

            if (pedidoOptional.isPresent()) {
                Pedido pedido = pedidoOptional.get();
                pedido.setStatus(stausPedido);
                Pedido pedido1 = pedidoRepository.save(pedido);
                response.setStatus(HttpStatus.OK.value());
            }
            return "redirect:/estoque/listarPedidos";
        } catch (Exception e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "redirect:/estoque/listarPedidos";
        }
    }

    @GetMapping("/listarPedidos")
    public String listarPedidos(Model model) {
        List pedidos = pedidoRepository.findAllOrderByIdDesc();
        model.addAttribute("pedidos", pedidos);
        return "listaPedidoEstoquista";
    }
}
