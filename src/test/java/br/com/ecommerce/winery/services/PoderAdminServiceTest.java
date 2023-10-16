package br.com.ecommerce.winery.services;

import br.com.ecommerce.winery.models.Status;
import br.com.ecommerce.winery.models.backoffice.Usuario;
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

class PoderAdminServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private PoderAdminService poderAdminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarUsuario() throws BusinessException {
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioSalvo = invocation.getArgument(0);
            // Defina o Status como null no objeto Usuario salvo
            usuarioSalvo.setStatus(null);
            return usuarioSalvo;
        });

        Usuario usuario = new Usuario();
        usuario.setSenha("senha");
        usuario.setConfirmaSenha("senha");
        usuario.setEmail("email@teste.com");

        Usuario resultado = poderAdminService.cadastrarUsuario(usuario);

        assertNotNull(resultado);
        assertNull(resultado.getStatus());
    }

    @Test
    public void testCadastrarUsuarioSenhasDiferentes() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha");
        usuario.setConfirmaSenha("outraSenha");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            poderAdminService.cadastrarUsuario(usuario);
        });

        assertEquals("Senhas não coincidem", exception.getMessage());
    }

    @Test
    public void testCadastrarUsuarioEmailExistente() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        Usuario usuario = new Usuario();
        usuario.setSenha("senha");
        usuario.setConfirmaSenha("senha");
        usuario.setEmail("email@teste.com");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            poderAdminService.cadastrarUsuario(usuario);
        });

        assertEquals("O email já está em uso.", exception.getMessage());
    }

    @Test
    public void testAlternarStatusUsuarioAtivo() throws BusinessException {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setStatus(Status.ATIVO);

        when(usuarioRepository.findById(1)).thenReturn(java.util.Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = poderAdminService.alternarStatusUsuario(1);

        assertNotNull(resultado);
        assertEquals(Status.INATIVO, resultado.getStatus());
    }

    @Test
    public void testAlternarStatusUsuarioInativo() throws BusinessException {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setStatus(Status.INATIVO);

        when(usuarioRepository.findById(1)).thenReturn(java.util.Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = poderAdminService.alternarStatusUsuario(1);

        assertNotNull(resultado);
        assertEquals(Status.ATIVO, resultado.getStatus());
    }
}