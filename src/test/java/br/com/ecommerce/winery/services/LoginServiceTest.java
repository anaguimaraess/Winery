package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Grupo;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() throws BusinessException {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setEmail("test@example.com");
        mockUsuario.setSenha("hashedPassword");

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(mockUsuario);
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        boolean result = loginService.login("test@example.com", "password");

        assertTrue(result);
        verify(httpSession).setAttribute("usuarioLogado", mockUsuario);
    }

    @Test
    public void testLoginFailure() {
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(null);

        assertThrows(BusinessException.class, () -> {
            loginService.login("test@example.com", "password");
        });

        verifyZeroInteractions(httpSession);
    }

    @Test
    public void testLogoutSuccess() throws BusinessException {
        when(httpSession.getAttribute("usuarioLogado")).thenReturn(new Usuario());

        assertDoesNotThrow(() -> {
            loginService.logout();
        });

        verify(httpSession).removeAttribute("usuarioLogado");
    }

    @Test
    public void testEstaLogado() {
        when(httpSession.getAttribute("usuarioLogado")).thenReturn(new Usuario());

        boolean isLoggedIn = loginService.estaLogado();

        assertTrue(isLoggedIn);
    }

    @Test
    public void testNaoEstaLogado() {
        when(httpSession.getAttribute("usuarioLogado")).thenReturn(null);

        boolean isLoggedIn = loginService.estaLogado();

        assertFalse(isLoggedIn);
    }

    @Test
    public void testEhAdmin() throws BusinessException {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setGrupo(Grupo.ADMIN);
        when(httpSession.getAttribute("usuarioLogado")).thenReturn(mockUsuario);

        boolean isAdmin = loginService.ehAdmin();

        assertTrue(isAdmin);
    }

    @Test
    public void testNaoehUsuario() throws BusinessException {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setGrupo(Grupo.USUARIO);
        when(httpSession.getAttribute("usuarioLogado")).thenReturn(mockUsuario);

        boolean isAdmin = loginService.ehUsuario();

        assertTrue(isAdmin);
    }

    @Test
    public void testNaoehEstoquista() throws BusinessException {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setGrupo(Grupo.ESTOQUISTA);
        when(httpSession.getAttribute("usuarioLogado")).thenReturn(mockUsuario);

        boolean isAdmin = loginService.ehEstoquista();

        assertTrue(isAdmin);
    }
}

