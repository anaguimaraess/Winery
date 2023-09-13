package br.com.ecommerce.winery.controllers.admin;
import br.com.ecommerce.winery.models.Produto;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.ProdutoRepository;
import br.com.ecommerce.winery.services.PoderAdminService;
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
@RequestMapping(path = "/estoque")
public class EstoquistaController {

    @Autowired
    private PoderAdminService cadastroProdutoService;

    @Autowired
    private ProdutoRepository produtoRepostory;

    @GetMapping("/listarProdutosDecrescente")
    public String listarTodosOsProdutosDecrescente(Model model) throws BusinessException {

        List<Produto> produtos = cadastroProdutoService.listarTodosProdutosDecrescente();
        model.addAttribute("produtos", produtos);
        return "listaProdutosDecrescente";
    }

    @GetMapping("/filtroNomeProduto")
    public String filtroNomeDoProduto(@RequestParam("nomeProduto") String nomeProduto, Model model, HttpServletResponse response) {
        nomeProduto = nomeProduto.toLowerCase();
        List<Produto> produtos = this.produtoRepostory.findByNomeProdutoContainingIgnoreCase(nomeProduto);
        model.addAttribute("produtos", produtos);
        response.setStatus(HttpStatus.OK.value());
        return "listaProdutos";
    }

    @PutMapping("/alterarEstoque")
    public ResponseEntity<?> alterarQuantidade(@RequestBody Map<String, Integer> request){
        try{
            int idProduto = request.get("idProduto");
            int novaQuantidade = request.get("qtdEstoque");

            cadastroProdutoService.alterarQuantidade(idProduto,novaQuantidade);
            return ResponseEntity.status(HttpStatus.OK).body("Quantidade atualizada com sucesso!");
        }catch (BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao alterar o produto!");
        }
    }


}
