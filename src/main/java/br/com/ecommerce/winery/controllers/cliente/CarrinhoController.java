package br.com.ecommerce.winery.controllers.cliente;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.cliente.FlagEndereco;
import br.com.ecommerce.winery.repositories.EnderecoRepository;
import br.com.ecommerce.winery.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class CarrinhoController {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoRepository enderecoRepository;
    @GetMapping("/carrinho")
    public String exibirCarrinho( Model model, Principal principal) {
        try {
            if (principal != null) {
                String clienteUsername = principal.getName();
                Cliente cliente = clienteService.obterClientePorEmail(clienteUsername);
                List<Endereco> listaEnderecos = enderecoRepository.findByClienteStatusAndFlagEndereco(cliente.getIdCliente(), Status.ATIVO, FlagEndereco.ENTREGA);
                cliente.setEnderecos(listaEnderecos);
                if (cliente != null) {
                    model.addAttribute("cliente", cliente);
                }
                return "carrinhoCompras";
            }
            return "carrinhoCompras";
        } catch (Exception e) {
            return "landingPageProduto";
        }
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
