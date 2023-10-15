package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.Cliente;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

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
    public Cliente cadastrarCliente(@RequestBody Cliente cliente) throws BusinessException {
        return clienteService.cadastrarCliente(cliente);
    }



}
