package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.dto.EnderecoDTO;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/Winery")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/cliente")
    public String cadastroCliente() {
        return "cadastroCliente";
    }

    @PostMapping("/alterarCliente")
    public ResponseEntity<String> alterarDadosCliente(@RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizarCliente(cliente);
            return ResponseEntity.ok("Sucesso: cliente alterado com sucesso! ");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/cadastrarCliente")
    public  ResponseEntity<String> cadastrarCliente(@RequestBody Cliente cliente) throws BusinessException {
        try {
            System.out.println("Recebida requisição para cadastrar cliente: {}"+ cliente);

            clienteService.cadastrarCliente(cliente);
            return ResponseEntity.ok("Sucesso: Cliente cadastrado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }}

        @PostMapping("/adicionarEndereco")
        public ResponseEntity<String> adicionarEnderecoAoCliente(@RequestBody EnderecoDTO enderecoDTO) {
            try {
                Cliente clienteCadastrado = clienteService.obterClientePorId(enderecoDTO.clienteId);
                System.out.println(clienteCadastrado);
                System.out.println(enderecoDTO.endereco);
                if (clienteCadastrado != null) {
                    clienteService.incluirEndereco(clienteCadastrado, enderecoDTO.endereco);
                    return ResponseEntity.ok("Endereço adicionado com sucesso!");
                } else {
                    return ResponseEntity.badRequest().body("Cliente não encontrado.");
                }
            } catch (BusinessException e) {
                return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
            }
        }
}
