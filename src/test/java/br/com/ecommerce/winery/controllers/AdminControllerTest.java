package br.com.ecommerce.winery.controllers;

import br.com.ecommerce.winery.controllers.admin.AdminController;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.CadastroUsuarioService;
import br.com.ecommerce.winery.utils.MensagemRetorno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CadastroUsuarioService cadastroUsuarioService;

    @Mock
    private MensagemRetorno mensagemRetorno;

    @Mock
    private Model model;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCadastroForm() {
        String viewName = adminController.getCadastroForm();

        assertEquals("cadastroUsuario", viewName);
    }

    @Test
    void testCadastrarUsuarioSuccess() throws BusinessException {
        Usuario usuario = new Usuario();
        when(cadastroUsuarioService.cadastrarUsuario(usuario)).thenReturn(usuario);

        String viewName = adminController.cadastrarUsuario(usuario, model, response);

        assertEquals("cadastroUsuario", viewName);
        verify(mensagemRetorno).adicionarMensagem(model, "sucesso", "Usuário cadastrado com sucesso!");
        verify(response).setStatus(HttpStatus.CREATED.value());
    }

    @Test
    void testCadastrarUsuarioBusinessException() throws BusinessException {
        Usuario usuario = new Usuario();
        when(cadastroUsuarioService.cadastrarUsuario(usuario)).thenThrow(new BusinessException("Erro ao cadastrar usuário"));

        String viewName = adminController.cadastrarUsuario(usuario, model, response);

        assertEquals("cadastroUsuario", viewName);
        verify(mensagemRetorno).adicionarMensagem(model, "erro", "Erro ao cadastrar usuário: Erro ao cadastrar usuário");
        verify(response).setStatus(HttpStatus.BAD_REQUEST.value());
    }
}
