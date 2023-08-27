package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.Usuario;
import br.com.ecommerce.winery.models.exception.BusinessException;
import br.com.ecommerce.winery.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CadastroUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LoginService loginService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CadastroUsuarioService cadastroUsuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarUsuarioSuccess() throws BusinessException {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setSenha("password");
        usuario.setConfirmaSenha("password");

        when(loginService.ehAdmin()).thenReturn(true);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = cadastroUsuarioService.cadastrarUsuario(usuario);

        assertEquals("encodedPassword", usuario.getSenha());
        assertEquals("encodedPassword", usuario.getConfirmaSenha());
        assertEquals(Status.ATIVO, usuario.getStatus());
        assertSame(usuario, result);

        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testCadastrarUsuarioNotAdmin() throws BusinessException {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setSenha("password");
        usuario.setConfirmaSenha("password");

        when(loginService.ehAdmin()).thenReturn(false);

        assertThrows(BusinessException.class, () -> cadastroUsuarioService.cadastrarUsuario(usuario));

        verifyZeroInteractions(usuarioRepository);
    }
    @Test
    void testValidarSenhasIguaisSuccess() throws BusinessException {
        cadastroUsuarioService.validarSenhasIguais("password", "password");
    }

    @Test
    void testValidarSenhasIguaisFailure() {
        assertThrows(BusinessException.class, () -> cadastroUsuarioService.validarSenhasIguais("password", "differentPassword"));
    }

    @Test
    void testValidarEmailUnicoSuccess() throws BusinessException {
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(false);
        cadastroUsuarioService.validarEmailUnico("test@example.com");
    }

    @Test
    void testValidarEmailUnicoFailure() {
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertThrows(BusinessException.class, () -> cadastroUsuarioService.validarEmailUnico("test@example.com"));
    }
}
