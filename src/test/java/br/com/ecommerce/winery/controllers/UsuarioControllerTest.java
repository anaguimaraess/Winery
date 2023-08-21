package br.com.ecommerce.winery.controllers;

import br.com.ecommerce.winery.controllers.admin.UsuarioController;
import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.services.admin.CadastroUsuarioService;
import br.com.ecommerce.winery.services.admin.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsuarioService usuarioService;
   @Test
    public void testReativarUsuario() throws Exception {
        Usuario usuarioAtivo = new Usuario();
        usuarioAtivo.setStatus(Status.ATIVO);

        Mockito.when(usuarioService.reativarUsuario(anyInt())).thenReturn(usuarioAtivo);

        mockMvc.perform(put("/usuarios/{id}/reativar", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testInativarUsuario() throws Exception{
       Usuario usuarioInativo = new Usuario();
       usuarioInativo.setStatus(Status.INATIVO);

       Mockito.when(usuarioService.inativarUsuario(anyInt())).thenReturn(usuarioInativo);

       mockMvc.perform(put("/usuarios/{id}/inativar",1))
               .andExpect(status().isOk());
    }

    @Test
    public void testAtualizarUsuario() throws Exception {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1);
        usuarioAtualizado.setNome("Novo Nome");
        usuarioAtualizado.setEmail("novo@email.com");

        Mockito.when(usuarioService.atualizarUsuario(anyInt(), Mockito.any(Usuario.class)))
                .thenReturn(usuarioAtualizado);

        String requestBody = "{ \"nome\": \"Novo Nome\", \"email\": \"novo@email.com\" }";

        mockMvc.perform(put("/usuarios/alterar/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }


}
