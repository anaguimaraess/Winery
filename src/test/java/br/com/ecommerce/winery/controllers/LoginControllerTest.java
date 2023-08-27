package br.com.ecommerce.winery.controllers;

import br.com.ecommerce.winery.controllers.login.LoginController;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import br.com.ecommerce.winery.services.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginControllerTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogarSuccess() throws BusinessException {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setEmail("test@example.com");
        mockUsuario.setSenha("password");

        when(loginService.login("test@example.com", "password")).thenReturn(true);

        ResponseEntity<?> responseEntity = loginController.logar(mockUsuario);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, responseEntity.getBody());
    }

    @Test
    public void testLogarFailure() throws BusinessException {
        when(loginService.login("test@example.com", "password")).thenThrow(new BusinessException("Login inválido"));
        Usuario user = new Usuario();
        user.setSenha("password");
        user.setEmail("test@example.com");
        ResponseEntity<?> responseEntity = loginController.logar(user);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Login inválido", responseEntity.getBody());
    }

    @Test
    public void testDeslogarSuccess() throws BusinessException {
        doNothing().when(loginService).logout();

        ResponseEntity<?> responseEntity = loginController.deslogar();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Usuário deslogado!", responseEntity.getBody());
    }

    @Test
    public void testDeslogarFailure() throws BusinessException {
        doThrow(new BusinessException("Erro ao deslogar")).when(loginService).logout();

        ResponseEntity<?> responseEntity = loginController.deslogar();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Erro ao deslogar", responseEntity.getBody());
    }
}