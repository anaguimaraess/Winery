package br.com.ecommerce.winery.controllers;
import br.com.ecommerce.winery.models.Produto;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.services.PoderAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(path = "")
public class HomeController {
    @Autowired
    private PoderAdminService cadastroProdutoService;
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHomeForm() {
        return "homeBackOffice";
    }

    @GetMapping("/Winery")
    public String listarTodosUsuarios(Model model, @RequestParam(defaultValue = "0") int page, HttpServletResponse response) {
        Pageable pageable = PageRequest.of(page, 10); // 10 produtos por página
        Page<Produto> produtosPage = cadastroProdutoService.listarTodosProdutosDecrescente(pageable);
        model.addAttribute("produtos", produtosPage); // Adicione a página ao modelo, não apenas a lista
        response.setStatus(HttpStatus.OK.value());
        return "landingPageProduto";
    }

    @GetMapping("/Winery/produto")
    public String exibirProduto(@RequestParam("id") int id, Model model, HttpServletResponse response) {
        try {
            Produto produto = cadastroProdutoService.buscarProdutoPorId(id);
            model.addAttribute("produto", produto);
            response.setStatus(HttpStatus.OK.value());
            return "DetalhesProduto";
        } catch (BusinessException e) {
            return "erro"; // Você pode criar uma página "erro.html" para lidar com erros.
        }
    }
}
