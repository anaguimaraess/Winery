package br.com.ecommerce.winery.controllers;

import br.com.ecommerce.winery.controllers.admin.AdminController;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.services.admin.CadastroUsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CadastroUsuarioService cadastroUsuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        AdminController adminController = new AdminController();
        ReflectionTestUtils.setField(adminController, "cadastroUsuarioService", cadastroUsuarioService);

        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testCadastrarUsuario_BusinessException() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@gmail.com");
        usuario.setSenha("senha123");

        String errorMessage = "Erro de negócio";

        when(cadastroUsuarioService.cadastrarUsuario(any(Usuario.class)))
                .thenThrow(new BusinessException(errorMessage));

        mockMvc.perform(post("/admin/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));
    }

}
