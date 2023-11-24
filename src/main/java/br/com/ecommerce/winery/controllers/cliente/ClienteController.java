package br.com.ecommerce.winery.controllers.cliente;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.cliente.CustomClientDetails;
import br.com.ecommerce.winery.models.cliente.Endereco;
import br.com.ecommerce.winery.models.cliente.FlagEndereco;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.models.pedido.ItemPedido;
import br.com.ecommerce.winery.models.pedido.Pedido;
import br.com.ecommerce.winery.models.pedido.StatusPedido;
import br.com.ecommerce.winery.models.pedido.formasPagamento.CartaoDeCredito;
import br.com.ecommerce.winery.repositories.CartaoRepository;
import br.com.ecommerce.winery.repositories.EnderecoRepository;
import br.com.ecommerce.winery.repositories.ItemPedidoRepository;
import br.com.ecommerce.winery.repositories.PedidoRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "")
public class ClienteController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    @Autowired
    private CartaoRepository cartaoRepository;

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
                List<Endereco> listaEnderecos = enderecoRepository
                        .findByClienteStatusAndFlagEndereco(cliente.getIdCliente(), Status.ATIVO, FlagEndereco.ENTREGA);
                cliente.setEnderecos(listaEnderecos);
                if (cliente != null) {
                    model.addAttribute("cliente", cliente);
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
    public String alterarSenha(Authentication authentication, String senhaAntiga, String confirmaSenhaNova,
                               String senhaNova, Model model) throws BusinessException {
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
        return "redirect:/erro";
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
    public ResponseEntity<String> adicionarEnderecoAoCliente(@RequestBody Endereco endereco,
                                                             Authentication authentication) {
        try {
            if (authentication != null && authentication.getPrincipal() instanceof CustomClientDetails) {
                CustomClientDetails userDetails = (CustomClientDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                Cliente cliente = clienteService.obterClientePorEmail(username);

                if (cliente != null) {
                    endereco.setCliente(cliente);
                    clienteService.incluirEndereco(endereco);

                }
            }
            return ResponseEntity.ok("Sucesso: Endereço adicionado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/cliente/enderecos")
    @ModelAttribute("enderecos")
    public List<Endereco> listarEnderecos(@ModelAttribute Cliente cliente, HttpServletResponse response)
            throws BusinessException {
        return clienteService.obterEnderecos(cliente.getIdCliente());
    }

    @GetMapping("/cliente/remover/{idEndereco}")
    public ResponseEntity<String> removerEndereco(@PathVariable int idEndereco) {
        clienteService.desativarEndereco(idEndereco);
        return ResponseEntity.ok("Sucesso: Endereço removido com sucesso!");
    }

    @PostMapping("/cliente/definirPadrao/{idEndereco}")
    public ResponseEntity<String> definirPadrao(@PathVariable int idEndereco, Authentication authentication) {

        try {
            if (authentication != null && authentication.getPrincipal() instanceof CustomClientDetails) {
                CustomClientDetails userDetails = (CustomClientDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                Cliente cliente = clienteService.obterClientePorEmail(username);

                if (cliente != null) {
                    clienteService.definirPadrao(idEndereco, cliente);
                }
            }
            return ResponseEntity.ok("Sucesso: Endereço se tornou padrão com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/salvarJsonPedido")
    public ResponseEntity<String> salvarJson(@RequestBody Pedido pedido) {
        System.out.println(pedido);

        try {
            List<ItemPedido> itemsPedido = pedido.getItensPedido();

            int idEndereco = pedido.getIdEndereco();
            pedido.setIdEndereco(idEndereco);
            pedido.setItensPedido(null);
            pedido.setCartaoDeCredito(null);
            pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

            pedido.setDataPedido(new Date());
            pedido.setId(0);
            Pedido pedidoSalvo = pedidoRepository.save(pedido);

            pedidoSalvo.setNumeroParcelas(pedido.getNumeroParcelas());

            for (ItemPedido itemPedido : itemsPedido) {
                itemPedido.setId(0);
                itemPedido.setPedido(pedidoSalvo);
                itemPedidoRepository.save(itemPedido);
            }
            return ResponseEntity.ok().body("Sucesso:" + "Pedido número #" + pedido.getId() + " realizado com sucesso! =)");

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }


    @GetMapping("/cliente/pedidos")
    public String meusPedidos(Model model, Principal principal) {
        try {
            if (principal != null) {
                String clienteUsername = principal.getName();
                Cliente cliente = clienteService.obterClientePorEmail(clienteUsername);
                List<Endereco> listaEnderecos = enderecoRepository
                        .findByClienteStatusAndFlagEndereco(cliente.getIdCliente(), Status.ATIVO, FlagEndereco.ENTREGA);
                cliente.setEnderecos(listaEnderecos);


                List<Pedido> pedidos = pedidoRepository.findByIdDoClienteOrderByDataDoPedidoDesc(String.valueOf(cliente.getIdCliente()));
                System.out.println(pedidos);

                model.addAttribute("cliente", cliente);
                model.addAttribute("pedidos", pedidos);
                return "meusPedidos";
            }
            return "meusPedidos";
        } catch (Exception e) {
            System.out.println("_--------> :" + e);
            return "redirect:/Winery";
        }
    }

    @GetMapping("/cliente/meu-pedido")
    public String detalhePedidoPorId(Model model, Principal principal, @RequestParam int id) {
        try {
            if (principal != null) {
                String clienteUsername = principal.getName();
                Cliente cliente = clienteService.obterClientePorEmail(clienteUsername);

                Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);
                Pedido pedidoExibido = pedidoOptional.get();
                Endereco endereco = enderecoRepository
                        .findById(pedidoExibido.getIdEndereco()).get();

                model.addAttribute("cliente", cliente);
                model.addAttribute("pedido", pedidoExibido);
                model.addAttribute("endereco", endereco);
                model.addAttribute("itensPedido", pedidoExibido.getItensPedido());

                System.out.println();
                return "detalhesPedidoCliente";
            }
            return "meusPedidos";

        } catch (Exception e) {
            return "meusPedidos";
        }
    }


}

