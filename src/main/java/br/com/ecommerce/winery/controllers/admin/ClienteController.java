package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.dto.EnderecoDTO;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.CustomClientDetails;
import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "")
public class ClienteController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/Winery/cliente")
    public String cadastroCliente() {
        return "cadastroCliente";
    }

    @GetMapping("/cliente/winery")
    public String exibirPaginaAlteracaoDados(Model model, Principal principal) {
        try {
            if (principal != null) {
                String clienteUsername = principal.getName();
                Cliente cliente = clienteService.obterClientePorEmail(clienteUsername);
                List<Endereco> enderecos = cliente.getEnderecos();
                if (cliente != null) {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("enderecos", enderecos);

                }
            }
            return "alterarCliente";
        } catch (Exception e) {
            return "landingPageProduto";
        }
    }

    @PostMapping("/cliente/alterar")
    public ResponseEntity<String> alterarDados(@ModelAttribute Cliente cliente) {
        try {
            Cliente usuarioAtualizado = clienteService.atualizarCliente(cliente);
            return ResponseEntity.ok("Sucesso: Usuário alterado com sucesso! ");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/cliente/senha")
    public String alterarSenha(Authentication authentication, String senhaAntiga, String confirmaSenhaNova, String senhaNova, Model model) throws BusinessException {
        if (authentication != null && authentication.getPrincipal() instanceof CustomClientDetails) {
            CustomClientDetails userDetails = (CustomClientDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            Cliente cliente = clienteService.obterClientePorEmail(username);

            if (cliente != null) {
                if (passwordEncoder.matches(senhaAntiga, cliente.getSenha())) {
                    clienteService.atualizaSenha(cliente, senhaNova, confirmaSenhaNova);
                    return "redirect:/cliente/winery"; // Página de sucesso
                } else {
                    model.addAttribute("Erro:", "Senha antiga incorreta");
                }
            } else {
                model.addAttribute("Erro:", "Cliente não encontrado");
            }
        } else {
            model.addAttribute("Erro:", "Cliente não autenticado");
        }

        return "redirect:/erro"; // Página de erro
    }


    @PostMapping("/Winery/cadastrarCliente")
    public ResponseEntity<String> cadastrarCliente(@RequestBody Cliente cliente) throws BusinessException {
        try {
            System.out.println("Recebida requisição para cadastrar cliente: {}" + cliente);

            clienteService.cadastrarCliente(cliente);
            return ResponseEntity.ok("Sucesso: Cliente cadastrado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cliente/adicionarEndereco")
    public ResponseEntity<String> adicionarEnderecoAoCliente(@RequestBody Endereco endereco) {
        try {
            System.out.println(endereco);
            clienteService.incluirEndereco(endereco);
            return ResponseEntity.ok("Sucesso: Endereço adicionado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/cliente/enderecos")
    @ModelAttribute("enderecos")
    public List<Endereco> listarEnderecos(@ModelAttribute Cliente cliente, HttpServletResponse response) throws BusinessException {
        return clienteService.obterEnderecos(cliente.getIdCliente());
    }
}
