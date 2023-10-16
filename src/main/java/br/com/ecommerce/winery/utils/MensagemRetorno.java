package br.com.ecommerce.winery.utils;

import br.com.ecommerce.winery.models.backoffice.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
@Component
public class MensagemRetorno {
    public void adicionarMensagem(Model model, String tipoMensagem, String mensagem) {
        model.addAttribute("tipoMensagem", tipoMensagem);
        model.addAttribute("mensagem", mensagem);
        model.addAttribute("usuario", new Usuario());
    }
}
