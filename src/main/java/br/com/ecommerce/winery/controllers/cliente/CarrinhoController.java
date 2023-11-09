package br.com.ecommerce.winery.controllers.cliente;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CarrinhoController {
    @GetMapping("/carrinho")
    public String exibirCarrinho() {
        return "carrinhoCompras"; // Nome do arquivo HTML sem a extensão
    }

     @GetMapping("/pagamento")
    public String exibirFomasDePagamento() {
        return "formaPagamento"; // Nome do arquivo HTML sem a extensão
    }

    @GetMapping("/checkout")
    public String exibirCheckout(){
        return "checkoutPagamento";
    }
    
}
