package br.com.ecommerce.winery.security.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessaoController {

    @Autowired
    private Sessao sessao;

    @GetMapping("/verificar-sessao")
    public boolean verificarSessao() {
        return sessao.isSessaoAtiva();
    }
}
