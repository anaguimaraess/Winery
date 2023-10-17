package br.com.ecommerce.winery.controllers.admin;

import br.com.ecommerce.winery.models.backoffice.Usuario;
import br.com.ecommerce.winery.models.cliente.Cliente;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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
            clienteService.cadastrarCliente(cliente);
            return ResponseEntity.ok("Sucesso:Cliente cadastrado com sucesso!");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Erro:" + e.getMessage());
        }

    
    }
}
